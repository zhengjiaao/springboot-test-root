/**
 * 密码服务平台控制台交互逻辑
 *
 * 功能：平台概览、摘要、对称/非对称加解密、签名验签、MAC、密钥管理、
 *       应用管理、审计日志、接入签名调试。
 *
 * 安全设计：
 * 1. 浏览器端完成接入签名（SM3/HMAC-SM3，见 sm3.js），与平台 SignatureUtil 行为一致；
 * 2. 所有响应数据的渲染均通过 DOM API + textContent，天然防 XSS；
 * 3. appKey/appSecret 仅保存在本浏览器 localStorage，不发送到任何第三方。
 *
 * @author: zhengja
 */
(function () {
	'use strict';

	/* ==================== 常量与状态 ==================== */

	var CONFIG_KEY = 'crypto-platform-console-config';
	var DEFAULT_CONFIG = { baseUrl: '', appKey: 'demo-app', appSecret: 'demo-app-secret-123456' };

	/** 全局状态：接入配置、密钥缓存、算法矩阵 */
	var state = {
		config: null,
		keys: [],
		algorithms: []
	};

	/* ==================== 基础工具 ==================== */

	function $(id) {
		return document.getElementById(id);
	}

	/** 时间格式化（毫秒时间戳/ISO串 → yyyy-MM-dd HH:mm:ss） */
	function fmtTime(value) {
		if (value == null || value === '') {
			return '-';
		}
		var d = new Date(value);
		if (isNaN(d.getTime())) {
			return String(value);
		}
		function two(n) {
			return n < 10 ? '0' + n : '' + n;
		}
		return d.getFullYear() + '-' + two(d.getMonth() + 1) + '-' + two(d.getDate()) + ' ' +
			two(d.getHours()) + ':' + two(d.getMinutes()) + ':' + two(d.getSeconds());
	}

	/** 字节数格式化 */
	function formatBytes(n) {
		if (n >= 1024 * 1024) {
			return (n / (1024 * 1024)) + ' MB';
		}
		if (n >= 1024) {
			return (n / 1024) + ' KB';
		}
		return n + ' B';
	}

	var toastTimer = null;

	/** 轻提示 */
	function toast(message, type) {
		var el = $('toast');
		el.textContent = message;
		el.className = 'toast show' + (type === 'err' ? ' err' : (type === 'ok' ? ' ok' : ''));
		if (toastTimer) {
			clearTimeout(toastTimer);
		}
		toastTimer = setTimeout(function () {
			el.className = 'toast';
		}, 3200);
	}

	/** 创建按钮 */
	function makeButton(label, className, handler) {
		var btn = document.createElement('button');
		btn.type = 'button';
		btn.className = className;
		btn.textContent = label;
		btn.addEventListener('click', handler);
		return btn;
	}

	/** 复制文本（Clipboard API 优先，回退 textarea） */
	function copyText(text) {
		function fallback() {
			var ta = document.createElement('textarea');
			ta.value = text;
			ta.style.position = 'fixed';
			ta.style.opacity = '0';
			document.body.appendChild(ta);
			ta.select();
			try {
				document.execCommand('copy');
				toast('已复制', 'ok');
			} catch (e) {
				toast('复制失败，请手动选择复制', 'err');
			}
			document.body.removeChild(ta);
		}
		if (navigator.clipboard && navigator.clipboard.writeText) {
			navigator.clipboard.writeText(text).then(function () {
				toast('已复制', 'ok');
			}, fallback);
		} else {
			fallback();
		}
	}

	/** 结果区渲染：banner + 键值对（带复制按钮）+ 操作按钮；全部经 textContent 输出，防 XSS */
	function renderResult(container, banner, pairs, actions) {
		container.textContent = '';
		if (banner && banner.text) {
			var bannerEl = document.createElement('div');
			bannerEl.className = banner.type + '-banner';
			bannerEl.textContent = banner.text;
			container.appendChild(bannerEl);
		}
		(pairs || []).forEach(function (pair) {
			var row = document.createElement('div');
			row.className = 'kv';
			var k = document.createElement('span');
			k.className = 'k';
			k.textContent = pair[0];
			var v = document.createElement('span');
			v.className = 'v';
			v.textContent = pair[1] == null ? '' : String(pair[1]);
			row.appendChild(k);
			row.appendChild(v);
			if (pair[1]) {
				row.appendChild(makeButton('复制', 'btn small', function () {
					copyText(pair[1]);
				}));
			} else {
				row.appendChild(document.createElement('span'));
			}
			container.appendChild(row);
		});
		(actions || []).forEach(function (action) {
			var wrap = document.createElement('div');
			wrap.className = 'actions';
			wrap.appendChild(makeButton(action.label, action.className || 'btn small', action.handler));
			container.appendChild(wrap);
		});
	}

	/** 表格辅助 */
	function appendCell(tr, text, className) {
		var td = document.createElement('td');
		if (className) {
			td.className = className;
		}
		td.textContent = text == null || text === '' ? '-' : String(text);
		tr.appendChild(td);
		return td;
	}

	function appendEmptyRow(tbody, colSpan, text) {
		tbody.textContent = '';
		var tr = document.createElement('tr');
		var td = document.createElement('td');
		td.colSpan = colSpan;
		td.className = 'empty';
		td.textContent = text;
		tr.appendChild(td);
		tbody.appendChild(tr);
	}

	/* ==================== 接入签名与 HTTP ==================== */

	/** 生成32位hex随机串（nonce） */
	function genNonce() {
		var bytes = new Uint8Array(16);
		if (window.crypto && window.crypto.getRandomValues) {
			window.crypto.getRandomValues(bytes);
		} else {
			for (var i = 0; i < 16; i++) {
				bytes[i] = Math.floor(Math.random() * 256);
			}
		}
		return Sm3Js.toHex(bytes);
	}

	/** 规范化平台地址（去首尾空白与尾部斜杠，空串表示同源） */
	function normalizeBaseUrl(url) {
		var v = (url || '').trim();
		while (v.length > 0 && v.charAt(v.length - 1) === '/') {
			v = v.substring(0, v.length - 1);
		}
		return v;
	}

	/**
	 * 构造接入签名（与平台 SignatureUtil 行为一致）
	 * stringToSign = appKey
 timestamp
 nonce
 method
 uri(含query)
 SM3(body).hex
	 * signature    = HMAC-SM3(appSecret的UTF-8字节, stringToSign) 的小写16进制
	 */
	function buildSignature(method, path, bodyBytes) {
		var timestamp = String(Date.now());
		var nonce = genNonce();
		var bodyDigest = Sm3Js.digestHex(bodyBytes);
		var stringToSign = [state.config.appKey, timestamp, nonce, method, path, bodyDigest].join('\n');
		var signature = Sm3Js.hmacHex(Sm3Js.utf8(state.config.appSecret), Sm3Js.utf8(stringToSign));
		return { timestamp: timestamp, nonce: nonce, bodyDigest: bodyDigest, stringToSign: stringToSign, signature: signature };
	}

	/** 统一解析响应：HTTP与业务码双层校验，非200抛出带业务码的错误 */
	async function parseResponse(response) {
		var text = await response.text();
		var payload;
		try {
			payload = JSON.parse(text);
		} catch (e) {
			throw new Error('平台响应解析失败（HTTP ' + response.status + '）');
		}
		if (!payload || payload.code !== 200) {
			var code = payload && payload.code ? '[' + payload.code + '] ' : '';
			throw new Error(code + ((payload && payload.message) || ('HTTP ' + response.status)));
		}
		return payload.data;
	}

	/** 基础请求（管理端接口：概览/应用/审计，无需接入签名） */
	async function request(method, path, bodyObj) {
		var options = { method: method };
		if (bodyObj !== undefined && bodyObj !== null) {
			options.headers = { 'Content-Type': 'application/json;charset=UTF-8' };
			options.body = JSON.stringify(bodyObj);
		}
		var response;
		try {
			response = await fetch(normalizeBaseUrl(state.config.baseUrl) + path, options);
		} catch (e) {
			throw new Error('无法连接平台（' + (normalizeBaseUrl(state.config.baseUrl) || '当前站点') + '）：' + e.message);
		}
		return parseResponse(response);
	}

	/** 接入签名请求（密钥/密码运算接口，自动携带4个签名头） */
	async function api(method, path, bodyObj) {
		if (!state.config.appKey || !state.config.appSecret) {
			throw new Error('请先在"接入配置"中填写 appKey 与 appSecret');
		}
		var bodyStr = bodyObj === undefined || bodyObj === null ? '' : JSON.stringify(bodyObj);
		var bodyBytes = bodyStr ? Sm3Js.utf8(bodyStr) : new Uint8Array(0);
		var sig = buildSignature(method, path, bodyBytes);

		var headers = {
			'X-App-Key': state.config.appKey,
			'X-Timestamp': sig.timestamp,
			'X-Nonce': sig.nonce,
			'X-Signature': sig.signature
		};
		var options = { method: method, headers: headers };
		if (bodyStr) {
			headers['Content-Type'] = 'application/json;charset=UTF-8';
			options.body = bodyStr;
		}
		var response;
		try {
			response = await fetch(normalizeBaseUrl(state.config.baseUrl) + path, options);
		} catch (e) {
			throw new Error('无法连接平台（' + (normalizeBaseUrl(state.config.baseUrl) || '当前站点') + '）：' + e.message);
		}
		return parseResponse(response);
	}

	/* ==================== 接入配置 ==================== */

	function loadConfig() {
		var config = { baseUrl: DEFAULT_CONFIG.baseUrl, appKey: DEFAULT_CONFIG.appKey, appSecret: DEFAULT_CONFIG.appSecret };
		try {
			var saved = window.localStorage.getItem(CONFIG_KEY);
			if (saved) {
				var parsed = JSON.parse(saved);
				if (parsed && typeof parsed === 'object') {
					config.baseUrl = parsed.baseUrl || '';
					config.appKey = parsed.appKey || '';
					config.appSecret = parsed.appSecret || '';
				}
			}
		} catch (e) {
			/* 隐私模式等场景忽略，使用默认值 */
		}
		return config;
	}

	function persistConfig() {
		try {
			window.localStorage.setItem(CONFIG_KEY, JSON.stringify(state.config));
		} catch (e) {
			/* 忽略存储失败 */
		}
	}

	function applyConfigToForm() {
		$('cfgBaseUrl').value = state.config.baseUrl || '';
		$('cfgAppKey').value = state.config.appKey || '';
		$('cfgAppSecret').value = state.config.appSecret || '';
	}

	function readConfigFromForm() {
		state.config = {
			baseUrl: $('cfgBaseUrl').value.trim(),
			appKey: $('cfgAppKey').value.trim(),
			appSecret: $('cfgAppSecret').value
		};
	}

	function setConnStatus(ok, text) {
		var el = $('connStatus');
		el.textContent = text;
		el.className = 'badge ' + (ok ? 'badge-ok' : 'badge-warn');
	}

	/** 测试连接：调用需签名的密钥列表接口，直接验证签名正确性 */
	async function testConnection() {
		readConfigFromForm();
		persistConfig();
		setConnStatus(false, '验证中…');
		try {
			var keys = await api('GET', '/api/key/list');
			state.keys = keys || [];
			setConnStatus(true, '接入验证通过');
			toast('接入签名有效，平台连接正常', 'ok');
			renderKeyTable();
			fillAllKeySelects();
			loadOverview();
		} catch (e) {
			setConnStatus(false, '接入验证失败');
			toast(e.message, 'err');
		}
	}

	/* ==================== 平台概览 ==================== */

	async function loadOverview() {
		try {
			var data = await request('GET', '/api/platform/overview');
			state.algorithms = data.algorithms || [];
			renderOverview(data);
			fillKeyAlgoSelect();
		} catch (e) {
			var container = $('overviewCards');
			container.textContent = '';
			var div = document.createElement('div');
			div.className = 'empty';
			div.textContent = '概览加载失败：' + e.message;
			container.appendChild(div);
		}
	}

	function renderOverview(data) {
		var container = $('overviewCards');
		container.textContent = '';
		var cards = [
			['启用 / 应用总数', String(data.enabledAppCount) + ' / ' + String(data.appCount)],
			['托管密钥', String(data.keyCount)],
			['审计记录（当前/上限）', String(data.auditLogCount) + ' / ' + String(data.auditLogCapacity)],
			['防重放缓存（当前/容量）', String(data.nonceCacheSize) + ' / ' + String(data.nonceCacheCapacity)],
			['签名时间戳窗口', String(data.authTimestampWindowMinutes) + ' 分钟'],
			['请求体上限', formatBytes(data.maxBodySize)]
		];
		cards.forEach(function (item) {
			var card = document.createElement('div');
			card.className = 'card';
			var num = document.createElement('div');
			num.className = 'num';
			num.textContent = item[1];
			var label = document.createElement('div');
			label.className = 'label';
			label.textContent = item[0];
			card.appendChild(num);
			card.appendChild(label);
			container.appendChild(card);
		});
		renderAlgoTable(data.algorithms || []);
	}

	function renderAlgoTable(algorithms) {
		var tbody = $('algoTableBody');
		if (!algorithms.length) {
			appendEmptyRow(tbody, 4, '暂无算法信息');
			return;
		}
		tbody.textContent = '';
		algorithms.forEach(function (algo) {
			var tr = document.createElement('tr');
			appendCell(tr, algo.name, 'mono');
			appendCell(tr, algo.category);
			appendCell(tr, algo.keyCreatable ? '是' : '否');
			appendCell(tr, algo.description, 'wrap');
			tbody.appendChild(tr);
		});
	}

	/* ==================== 密钥管理 ==================== */

	/** 刷新密钥缓存并同步表格与下拉框 */
	async function refreshKeys(showToast) {
		try {
			var keys = await api('GET', '/api/key/list');
			state.keys = keys || [];
			renderKeyTable();
			fillAllKeySelects();
			if (showToast) {
				toast('密钥列表已刷新（' + state.keys.length + '把）', 'ok');
			}
		} catch (e) {
			if (showToast) {
				toast(e.message, 'err');
			}
		}
	}

	/** 按类别填充密钥下拉框（仅列启用状态密钥） */
	function fillKeySelect(selectEl, category) {
		var current = selectEl.value;
		var keys = state.keys.filter(function (key) {
			return key.status === 'ENABLED' && (!category || key.category === category);
		});
		selectEl.textContent = '';
		if (!keys.length) {
			var option = document.createElement('option');
			option.value = '';
			option.textContent = state.keys.length
				? '（无可用密钥，请先在"密钥管理"中创建）'
				: '（尚未加载密钥，请先"测试连接"或在"密钥管理"中刷新）';
			selectEl.appendChild(option);
			return;
		}
		keys.forEach(function (key) {
			var option = document.createElement('option');
			option.value = key.keyId;
			option.textContent = (key.alias || key.keyId) + ' · ' + key.algorithm + ' · ' + key.keyId.substring(0, 12) + '…';
			selectEl.appendChild(option);
		});
		if (current) {
			selectEl.value = current;
		}
	}

	function fillAllKeySelects() {
		fillKeySelect($('symEncKey'), 'SYMMETRIC');
		fillKeySelect($('symDecKey'), 'SYMMETRIC');
		fillKeySelect($('asymEncKey'), 'ASYMMETRIC');
		fillKeySelect($('asymDecKey'), 'ASYMMETRIC');
		fillKeySelect($('signKey'), 'ASYMMETRIC');
		fillKeySelect($('verifyKey'), 'ASYMMETRIC');
		fillKeySelect($('macKey'), 'MAC');
	}

	/** 创建密钥算法下拉框：优先使用平台返回的算法矩阵，失败时兜底 */
	function fillKeyAlgoSelect() {
		var select = $('keyAlgo');
		var creatable = state.algorithms.filter(function (algo) {
			return algo.keyCreatable;
		});
		if (!creatable.length) {
			['SM4', 'AES_128', 'AES_256', 'SM2', 'RSA_2048', 'HMAC_SM3', 'HMAC_SHA_256'].forEach(function (name) {
				creatable.push({ name: name });
			});
		}
		var current = select.value;
		select.textContent = '';
		creatable.forEach(function (algo) {
			var option = document.createElement('option');
			option.value = algo.name;
			option.textContent = algo.name;
			select.appendChild(option);
		});
		if (current) {
			select.value = current;
		}
	}

	function renderKeyTable() {
		var tbody = $('keyTableBody');
		if (!state.keys.length) {
			appendEmptyRow(tbody, 7, '暂无密钥，可在上方创建，或点击"测试连接"加载');
			return;
		}
		tbody.textContent = '';
		state.keys.forEach(function (key) {
			var tr = document.createElement('tr');
			appendCell(tr, key.keyId, 'mono');
			appendCell(tr, key.algorithm);
			appendCell(tr, key.category);
			appendCell(tr, key.alias);
			appendCell(tr, key.status);
			appendCell(tr, fmtTime(key.createTime));
			var td = document.createElement('td');
			if (key.status === 'ENABLED') {
				td.appendChild(makeButton('停用', 'btn small', function () {
					toggleKey(key.keyId, false);
				}));
			} else if (key.status === 'DISABLED') {
				td.appendChild(makeButton('启用', 'btn small', function () {
					toggleKey(key.keyId, true);
				}));
			}
			td.appendChild(document.createTextNode(' '));
			td.appendChild(makeButton('销毁', 'btn small danger', function () {
				destroyKey(key.keyId, key.alias);
			}));
			if (key.publicKey) {
				td.appendChild(document.createTextNode(' '));
				td.appendChild(makeButton('公钥', 'btn small', function () {
					renderResult($('keyOpResult'), null, [
						['keyId', key.keyId],
						['算法', key.algorithm],
						['公钥（可对外分发）', key.publicKey]
					]);
				}));
			}
			tr.appendChild(td);
			tbody.appendChild(tr);
		});
	}

	async function createKey() {
		var algorithm = $('keyAlgo').value;
		if (!algorithm) {
			toast('请选择算法', 'err');
			return;
		}
		try {
			var data = await api('POST', '/api/key/create', {
				algorithm: algorithm,
				alias: $('keyAlias').value.trim(),
				remark: $('keyRemark').value.trim()
			});
			var pairs = [
				['keyId', data.keyId],
				['算法', data.algorithm + '（' + data.category + '）'],
				['别名', data.alias],
				['状态', data.status]
			];
			if (data.publicKey) {
				pairs.push(['公钥（可对外分发）', data.publicKey]);
			}
			renderResult($('createKeyResult'), { type: 'ok', text: '密钥创建成功（密钥材料不出平台）' }, pairs);
			toast('密钥已创建', 'ok');
			await refreshKeys(false);
		} catch (e) {
			renderResult($('createKeyResult'), { type: 'err', text: '创建失败：' + e.message }, []);
			toast(e.message, 'err');
		}
	}

	async function toggleKey(keyId, enable) {
		try {
			await api('POST', '/api/key/' + encodeURIComponent(keyId) + (enable ? '/enable' : '/disable'));
			toast('密钥已' + (enable ? '启用' : '停用'), 'ok');
			await refreshKeys(false);
		} catch (e) {
			toast(e.message, 'err');
		}
	}

	async function destroyKey(keyId, alias) {
		if (!window.confirm('确定销毁密钥「' + (alias || keyId) + '」？\n密钥材料将被抹除且不可恢复！')) {
			return;
		}
		try {
			await api('DELETE', '/api/key/' + encodeURIComponent(keyId));
			toast('密钥已销毁', 'ok');
			await refreshKeys(false);
		} catch (e) {
			toast(e.message, 'err');
		}
	}

	/* ==================== 摘要 ==================== */

	async function doDigest() {
		var algorithm = $('digestAlgo').value;
		var data = $('digestData').value;
		if (!data) {
			toast('请输入原始数据', 'err');
			return;
		}
		try {
			var result = await api('POST', '/api/crypto/digest', { algorithm: algorithm, data: data });
			var pairs = [['算法', result.algorithm], [result.algorithm + ' 摘要（hex）', result.digest]];
			var banner = null;
			if (algorithm === 'SM3') {
				// 浏览器本地SM3 与平台摘要互验，同时验证前端签名算法的正确性
				var local = Sm3Js.digestHex(Sm3Js.utf8(data));
				var match = local === result.digest;
				banner = match
					? { type: 'ok', text: '浏览器本地 SM3 与平台结果一致（前端签名算法已互验通过）' }
					: { type: 'err', text: '浏览器本地 SM3 与平台结果不一致，页面脚本可能不完整' };
				pairs.push(['浏览器本地 SM3', local]);
			}
			renderResult($('digestResult'), banner, pairs);
		} catch (e) {
			renderResult($('digestResult'), { type: 'err', text: e.message }, []);
		}
	}

	/* ==================== 加解密（配置驱动，四个操作共用一套逻辑） ==================== */

	var CRYPTO_OPS = [
		{ button: 'btnSymEnc', select: 'symEncKey', input: 'symPlain', field: 'plainText',
			path: '/api/crypto/symmetric/encrypt', resultField: 'cipherText', resultLabel: '密文（Base64）',
			resultBox: 'symEncResult', fillTarget: 'symCipher' },
		{ button: 'btnSymDec', select: 'symDecKey', input: 'symCipher', field: 'cipherText',
			path: '/api/crypto/symmetric/decrypt', resultField: 'plainText', resultLabel: '明文',
			resultBox: 'symDecResult', fillTarget: 'symPlain' },
		{ button: 'btnAsymEnc', select: 'asymEncKey', input: 'asymPlain', field: 'plainText',
			path: '/api/crypto/asymmetric/encrypt', resultField: 'cipherText', resultLabel: '密文（Base64）',
			resultBox: 'asymEncResult', fillTarget: 'asymCipher' },
		{ button: 'btnAsymDec', select: 'asymDecKey', input: 'asymCipher', field: 'cipherText',
			path: '/api/crypto/asymmetric/decrypt', resultField: 'plainText', resultLabel: '明文',
			resultBox: 'asymDecResult', fillTarget: 'asymPlain' }
	];

	function bindCryptoOps() {
		CRYPTO_OPS.forEach(function (op) {
			$(op.button).addEventListener('click', function () {
				runCryptoOp(op);
			});
		});
	}

	async function runCryptoOp(op) {
		var keyId = $(op.select).value;
		var inputValue = $(op.input).value;
		if (!keyId) {
			toast('请先选择密钥', 'err');
			return;
		}
		if (!inputValue) {
			toast('请输入待处理数据', 'err');
			return;
		}
		var body = { keyId: keyId };
		body[op.field] = inputValue;
		try {
			var result = await api('POST', op.path, body);
			renderResult($(op.resultBox), null, [
				['keyId', result.keyId],
				['算法', result.algorithm],
				[op.resultLabel, result[op.resultField]]
			], [{
				label: '回填到对应输入框（演示往返）',
				handler: function () {
					$(op.fillTarget).value = result[op.resultField];
					toast('已回填', 'ok');
				}
			}]);
		} catch (e) {
			renderResult($(op.resultBox), { type: 'err', text: e.message }, []);
		}
	}

	/* ==================== 签名验签 ==================== */

	async function doSign() {
		var keyId = $('signKey').value;
		var data = $('signData').value;
		if (!keyId) {
			toast('请先选择密钥对', 'err');
			return;
		}
		if (!data) {
			toast('请输入待签名数据', 'err');
			return;
		}
		try {
			var result = await api('POST', '/api/crypto/sign', { keyId: keyId, data: data });
			renderResult($('signResult'), null, [
				['keyId', result.keyId],
				['签名算法', result.algorithm],
				['签名值（Base64）', result.signature]
			], [{
				label: '填入验签区',
				handler: function () {
					$('verifySignature').value = result.signature;
					$('verifyData').value = data;
					$('verifyKey').value = result.keyId;
					toast('已填入验签区', 'ok');
				}
			}]);
		} catch (e) {
			renderResult($('signResult'), { type: 'err', text: e.message }, []);
		}
	}

	async function doVerify() {
		var keyId = $('verifyKey').value;
		var data = $('verifyData').value;
		var signature = $('verifySignature').value.trim();
		if (!keyId) {
			toast('请先选择密钥对', 'err');
			return;
		}
		if (!data || !signature) {
			toast('请输入原始数据与签名值', 'err');
			return;
		}
		try {
			var result = await api('POST', '/api/crypto/verify', { keyId: keyId, data: data, signature: signature });
			var banner = result.verified
				? { type: 'ok', text: '验签通过：签名与数据、公钥匹配' }
				: { type: 'warn', text: '验签未通过：数据或签名不一致' };
			renderResult($('verifyResult'), banner, [
				['keyId', result.keyId],
				['算法', result.algorithm],
				['验签结果', String(result.verified)]
			]);
		} catch (e) {
			renderResult($('verifyResult'), { type: 'err', text: e.message }, []);
		}
	}

	/* ==================== 消息认证码 ==================== */

	async function doMac() {
		var keyId = $('macKey').value;
		var data = $('macData').value;
		if (!keyId) {
			toast('请先选择MAC密钥', 'err');
			return;
		}
		if (!data) {
			toast('请输入原始数据', 'err');
			return;
		}
		try {
			var result = await api('POST', '/api/crypto/mac', { keyId: keyId, data: data });
			renderResult($('macResult'), null, [
				['keyId', result.keyId],
				['算法', result.algorithm],
				['MAC值（hex）', result.mac]
			]);
		} catch (e) {
			renderResult($('macResult'), { type: 'err', text: e.message }, []);
		}
	}

	/* ==================== 应用管理 ==================== */

	async function loadApps(showToast) {
		try {
			var apps = await request('GET', '/api/app/list');
			renderAppTable(apps || []);
			if (showToast) {
				toast('应用列表已刷新', 'ok');
			}
		} catch (e) {
			if (showToast) {
				toast(e.message, 'err');
			}
		}
	}

	function renderAppTable(apps) {
		var tbody = $('appTableBody');
		if (!apps.length) {
			appendEmptyRow(tbody, 6, '暂无应用');
			return;
		}
		tbody.textContent = '';
		apps.forEach(function (app) {
			var tr = document.createElement('tr');
			appendCell(tr, app.appKey, 'mono');
			appendCell(tr, app.appName);
			appendCell(tr, app.contact);
			appendCell(tr, app.status);
			appendCell(tr, fmtTime(app.createTime));
			var td = document.createElement('td');
			if (app.status === 'ENABLED') {
				td.appendChild(makeButton('停用', 'btn small', function () {
					toggleApp(app.appKey, false);
				}));
			} else if (app.status === 'DISABLED') {
				td.appendChild(makeButton('启用', 'btn small', function () {
					toggleApp(app.appKey, true);
				}));
			}
			tr.appendChild(td);
			tbody.appendChild(tr);
		});
	}

	async function registerApp() {
		var appName = $('appName').value.trim();
		if (!appName) {
			toast('请输入应用名称', 'err');
			return;
		}
		try {
			var data = await request('POST', '/api/app/register', {
				appName: appName,
				contact: $('appContact').value.trim(),
				remark: $('appRemark').value.trim()
			});
			renderResult($('registerAppResult'),
				{ type: 'warn', text: 'appSecret 仅此一次返回，请立即妥善保存！' },
				[
					['appKey', data.appKey],
					['appSecret', data.appSecret],
					['应用名称', data.appName],
					['状态', data.status],
					['注册时间', fmtTime(data.createTime)]
				],
				[{
					label: '填入接入配置',
					handler: function () {
						state.config.baseUrl = $('cfgBaseUrl').value.trim();
						state.config.appKey = data.appKey;
						state.config.appSecret = data.appSecret;
						persistConfig();
						applyConfigToForm();
						toast('凭证已填入接入配置，可点击"测试连接"验证', 'ok');
					}
				}]);
			toast('应用注册成功', 'ok');
			await loadApps(false);
		} catch (e) {
			renderResult($('registerAppResult'), { type: 'err', text: '注册失败：' + e.message }, []);
		}
	}

	async function toggleApp(appKey, enable) {
		try {
			await request('POST', '/api/app/' + encodeURIComponent(appKey) + (enable ? '/enable' : '/disable'));
			toast('应用已' + (enable ? '启用' : '停用'), 'ok');
			await loadApps(false);
		} catch (e) {
			toast(e.message, 'err');
		}
	}

	/* ==================== 审计日志 ==================== */

	async function queryAudit() {
		var appKey = $('auditAppKey').value.trim();
		var limit = parseInt($('auditLimit').value, 10);
		if (!limit || limit < 1) {
			limit = 100;
		}
		if (limit > 1000) {
			limit = 1000;
		}
		var path = '/api/audit/list?limit=' + limit + (appKey ? '&appKey=' + encodeURIComponent(appKey) : '');
		try {
			var logs = await request('GET', path);
			renderAuditTable(logs || []);
			toast('共返回 ' + (logs ? logs.length : 0) + ' 条审计记录', 'ok');
		} catch (e) {
			toast(e.message, 'err');
		}
	}

	function renderAuditTable(logs) {
		var tbody = $('auditTableBody');
		if (!logs.length) {
			appendEmptyRow(tbody, 9, '暂无审计记录');
			return;
		}
		tbody.textContent = '';
		logs.forEach(function (log) {
			var tr = document.createElement('tr');
			appendCell(tr, fmtTime(log.timestamp));
			appendCell(tr, log.appKey, 'mono');
			appendCell(tr, log.action);
			appendCell(tr, log.keyId, 'mono');
			appendCell(tr, log.algorithm);
			appendCell(tr, log.success ? '成功' : '失败');
			appendCell(tr, log.costMs + ' ms');
			appendCell(tr, log.clientIp);
			var msgCell = appendCell(tr, log.message, 'wrap');
			if (log.message) {
				msgCell.title = log.message;
			}
			tbody.appendChild(tr);
		});
	}

	/* ==================== 签名调试 ==================== */

	function doSignDebug() {
		readConfigFromForm();
		if (!state.config.appKey || !state.config.appSecret) {
			renderResult($('signDebugResult'), { type: 'err', text: '请先在上方填写 appKey 与 appSecret' }, []);
			return;
		}
		var method = $('dbgMethod').value;
		var uri = $('dbgUri').value.trim() || '/';
		if (uri.charAt(0) !== '/') {
			uri = '/' + uri;
		}
		var bodyStr = $('dbgBody').value;
		var bodyBytes = bodyStr ? Sm3Js.utf8(bodyStr) : new Uint8Array(0);
		var sig = buildSignature(method, uri, bodyBytes);
		renderResult($('signDebugResult'),
			{ type: 'ok', text: '以下请求头可直接用于：' + method + ' ' + uri },
			[
				['X-App-Key', state.config.appKey],
				['X-Timestamp', sig.timestamp],
				['X-Nonce', sig.nonce],
				['X-Signature', sig.signature],
				['SM3(body)', sig.bodyDigest],
				['stringToSign', sig.stringToSign]
			]);
	}

	/* ==================== Tab 与初始化 ==================== */

	function switchTab(name) {
		var buttons = document.querySelectorAll('#tabs button');
		Array.prototype.forEach.call(buttons, function (btn) {
			btn.classList.toggle('active', btn.getAttribute('data-tab') === name);
		});
		var pages = document.querySelectorAll('.tab-page');
		Array.prototype.forEach.call(pages, function (page) {
			page.classList.toggle('active', page.id === 'tab-' + name);
		});
		onTabEnter(name);
	}

	/** 进入页签时的数据加载策略：懒加载，避免无谓请求 */
	function onTabEnter(name) {
		if (name === 'overview') {
			loadOverview();
		} else if (name === 'keys' || name === 'symmetric' || name === 'asymmetric' || name === 'sign' || name === 'mac') {
			if (!state.keys.length && state.config.appKey) {
				refreshKeys(false);
			}
		} else if (name === 'apps') {
			loadApps(false);
		}
	}

	function bindEvents() {
		var buttons = document.querySelectorAll('#tabs button');
		Array.prototype.forEach.call(buttons, function (btn) {
			btn.addEventListener('click', function () {
				switchTab(btn.getAttribute('data-tab'));
			});
		});

		$('btnSaveCfg').addEventListener('click', function () {
			readConfigFromForm();
			persistConfig();
			toast('配置已保存到本浏览器（localStorage）', 'ok');
		});
		$('btnTestCfg').addEventListener('click', testConnection);
		$('btnToggleSecret').addEventListener('click', function () {
			var input = $('cfgAppSecret');
			var show = input.type === 'password';
			input.type = show ? 'text' : 'password';
			this.textContent = show ? '隐藏密钥' : '显示密钥';
		});
		$('btnRefreshOverview').addEventListener('click', function () {
			loadOverview();
			toast('概览已刷新', 'ok');
		});

		$('btnDigest').addEventListener('click', doDigest);
		bindCryptoOps();
		$('btnSign').addEventListener('click', doSign);
		$('btnVerify').addEventListener('click', doVerify);
		$('btnMac').addEventListener('click', doMac);

		$('btnCreateKey').addEventListener('click', createKey);
		$('btnReloadKeys').addEventListener('click', function () {
			refreshKeys(true);
		});

		$('btnRegisterApp').addEventListener('click', registerApp);
		$('btnReloadApps').addEventListener('click', function () {
			loadApps(true);
		});

		$('btnQueryAudit').addEventListener('click', queryAudit);
		$('btnSignDebug').addEventListener('click', doSignDebug);
	}

	function init() {
		state.config = loadConfig();
		applyConfigToForm();

		// 浏览器端 SM3 自检：失败说明页面脚本被篡改或不完整，明确告警
		var selfCheck = $('sm3SelfCheck');
		if (Sm3Js.selfTest()) {
			selfCheck.textContent = 'SM3 算法自检通过（浏览器端签名可用）';
			selfCheck.className = 'badge badge-ok';
		} else {
			selfCheck.textContent = 'SM3 算法自检失败：页面脚本可能被篡改或不完整，请勿使用';
			selfCheck.className = 'badge badge-err';
		}

		bindEvents();
		fillKeyAlgoSelect();
		fillAllKeySelects();
		loadOverview();
	}

	document.addEventListener('DOMContentLoaded', init);
})();

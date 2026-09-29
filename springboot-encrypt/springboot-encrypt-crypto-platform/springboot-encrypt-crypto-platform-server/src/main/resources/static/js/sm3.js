/**
 * SM3 国密摘要算法 + HMAC-SM3（纯 JavaScript 实现，无任何外部依赖）
 *
 * 用途：控制台页面在浏览器端完成在线接入签名，行为与平台端
 *   SignatureUtil / Sm3DigestUtil / HmacUtil 完全一致：
 *   signature = HMAC-SM3(appSecret的UTF-8字节, stringToSign) 的小写16进制
 *
 * 标准验证向量（GB/T 32905-2016 附录A）：
 *   SM3("abc") = 66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0
 */
(function (global) {
	'use strict';

	/** 32位循环左移 */
	function rotl(x, n) {
		n &= 31;
		return ((x << n) | (x >>> (32 - n))) >>> 0;
	}

	/** 置换函数 P0 */
	function p0(x) {
		return (x ^ rotl(x, 9) ^ rotl(x, 17)) >>> 0;
	}

	/** 置换函数 P1 */
	function p1(x) {
		return (x ^ rotl(x, 15) ^ rotl(x, 23)) >>> 0;
	}

	/** 布尔函数 FF */
	function ff(x, y, z, j) {
		return j < 16 ? (x ^ y ^ z) >>> 0 : ((x & y) | (x & z) | (y & z)) >>> 0;
	}

	/** 布尔函数 GG */
	function gg(x, y, z, j) {
		return j < 16 ? (x ^ y ^ z) >>> 0 : ((x & y) | (~x & z)) >>> 0;
	}

	/** 初始向量 IV */
	var IV = [0x7380166f, 0x4914b2b9, 0x172442d7, 0xda8a0600,
		0xa96f30bc, 0x163138aa, 0xe38dee4d, 0xb0fb0e4e];

	/** 消息填充：追加 0x80，补零至长度≡56 (mod 64)，末尾8字节大端比特长度 */
	function pad(msg) {
		var len = msg.length;
		var bitLen = len * 8;
		var totalLen = ((len + 9 + 63) >> 6) << 6;
		var out = new Uint8Array(totalLen);
		out.set(msg);
		out[len] = 0x80;

		var high = Math.floor(bitLen / 0x100000000);
		var low = bitLen >>> 0;
		var o = totalLen - 8;
		out[o] = (high >>> 24) & 0xff;
		out[o + 1] = (high >>> 16) & 0xff;
		out[o + 2] = (high >>> 8) & 0xff;
		out[o + 3] = high & 0xff;
		out[o + 4] = (low >>> 24) & 0xff;
		out[o + 5] = (low >>> 16) & 0xff;
		out[o + 6] = (low >>> 8) & 0xff;
		out[o + 7] = low & 0xff;
		return out;
	}

	/** 压缩函数：处理一个 512bit 分组 */
	function compress(state, block, offset) {
		var w = new Array(68);
		var i;
		for (i = 0; i < 16; i++) {
			var o = offset + i * 4;
			w[i] = ((block[o] << 24) | (block[o + 1] << 16) | (block[o + 2] << 8) | block[o + 3]) >>> 0;
		}
		// 消息扩展：W[16..67]
		for (i = 16; i < 68; i++) {
			w[i] = (p1((w[i - 16] ^ w[i - 9] ^ rotl(w[i - 3], 15)) >>> 0) ^ rotl(w[i - 13], 7) ^ w[i - 6]) >>> 0;
		}

		var a = state[0], b = state[1], c = state[2], d = state[3];
		var e = state[4], f = state[5], g = state[6], h = state[7];
		for (i = 0; i < 64; i++) {
			var t = i < 16 ? 0x79cc4519 : 0x7a879d8a;
			var a12 = rotl(a, 12);
			var ss1 = rotl((a12 + e + rotl(t, i)) >>> 0, 7);
			var ss2 = (ss1 ^ a12) >>> 0;
			var wj = w[i];
			var wj4 = (w[i] ^ w[i + 4]) >>> 0; // W'[j]
			var tt1 = (ff(a, b, c, i) + d + ss2 + wj4) >>> 0;
			var tt2 = (gg(e, f, g, i) + h + ss1 + wj) >>> 0;

			d = c;
			c = rotl(b, 9);
			b = a;
			a = tt1;
			h = g;
			g = rotl(f, 19);
			f = e;
			e = p0(tt2);
		}

		state[0] = (state[0] ^ a) >>> 0;
		state[1] = (state[1] ^ b) >>> 0;
		state[2] = (state[2] ^ c) >>> 0;
		state[3] = (state[3] ^ d) >>> 0;
		state[4] = (state[4] ^ e) >>> 0;
		state[5] = (state[5] ^ f) >>> 0;
		state[6] = (state[6] ^ g) >>> 0;
		state[7] = (state[7] ^ h) >>> 0;
	}

	/** 计算 SM3 摘要，返回32字节 Uint8Array */
	function digest(msgBytes) {
		var data = pad(msgBytes);
		var state = IV.slice();
		for (var i = 0; i < data.length; i += 64) {
			compress(state, data, i);
		}
		var out = new Uint8Array(32);
		for (var j = 0; j < 8; j++) {
			out[j * 4] = (state[j] >>> 24) & 0xff;
			out[j * 4 + 1] = (state[j] >>> 16) & 0xff;
			out[j * 4 + 2] = (state[j] >>> 8) & 0xff;
			out[j * 4 + 3] = state[j] & 0xff;
		}
		return out;
	}

	/** HMAC-SM3（分组长64字节，标准HMAC构造），返回32字节 Uint8Array */
	function hmac(keyBytes, msgBytes) {
		var blockSize = 64;
		var key = keyBytes;
		if (key.length > blockSize) {
			key = digest(key);
		}
		var padded = new Uint8Array(blockSize);
		padded.set(key);

		var ipad = new Uint8Array(blockSize + msgBytes.length);
		for (var i = 0; i < blockSize; i++) {
			ipad[i] = padded[i] ^ 0x36;
		}
		ipad.set(msgBytes, blockSize);

		var opad = new Uint8Array(blockSize + 32);
		for (var j = 0; j < blockSize; j++) {
			opad[j] = padded[j] ^ 0x5c;
		}
		opad.set(digest(ipad), blockSize);

		return digest(opad);
	}

	/** 字节数组转小写16进制串 */
	function toHex(bytes) {
		var hex = '';
		for (var i = 0; i < bytes.length; i++) {
			hex += (bytes[i] < 16 ? '0' : '') + bytes[i].toString(16);
		}
		return hex;
	}

	/** 字符串转 UTF-8 字节数组（TextEncoder 优先，含旧浏览器手写回退） */
	function utf8(str) {
		if (global.TextEncoder) {
			return new global.TextEncoder().encode(str);
		}
		var bytes = [];
		for (var i = 0; i < str.length; i++) {
			var c = str.charCodeAt(i);
			if (c < 0x80) {
				bytes.push(c);
			} else if (c < 0x800) {
				bytes.push(0xc0 | (c >> 6), 0x80 | (c & 0x3f));
			} else if (c >= 0xd800 && c <= 0xdbff && i + 1 < str.length) {
				var c2 = str.charCodeAt(++i);
				var cp = 0x10000 + ((c - 0xd800) << 10) + (c2 - 0xdc00);
				bytes.push(0xf0 | (cp >> 18), 0x80 | ((cp >> 12) & 0x3f), 0x80 | ((cp >> 6) & 0x3f), 0x80 | (cp & 0x3f));
			} else {
				bytes.push(0xe0 | (c >> 12), 0x80 | ((c >> 6) & 0x3f), 0x80 | (c & 0x3f));
			}
		}
		return new Uint8Array(bytes);
	}

	/** 标准向量自检：SM3("abc") */
	function selfTest() {
		var expected = '66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0';
		return toHex(digest(utf8('abc'))) === expected;
	}

	global.Sm3Js = {
		digest: digest,
		digestHex: function (bytes) { return toHex(digest(bytes)); },
		hmac: hmac,
		hmacHex: function (keyBytes, msgBytes) { return toHex(hmac(keyBytes, msgBytes)); },
		utf8: utf8,
		toHex: toHex,
		selfTest: selfTest
	};
})(window);

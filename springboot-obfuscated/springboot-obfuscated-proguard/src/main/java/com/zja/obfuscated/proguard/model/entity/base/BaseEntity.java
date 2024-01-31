/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-09 15:34
 * @Since:
 */
package com.zja.obfuscated.proguard.model.entity.base;

import com.zja.obfuscated.proguard.util.IdGeneratorUtil;
import org.springframework.data.domain.Persistable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Objects;

/**
 * 抽象id父类
 *
 * @author: zhengja
 * @since: 2023/08/09 15:34
 */
@MappedSuperclass
public class BaseEntity implements Persistable<String>, Serializable {

    @Id
    protected String id;

    @Transient
    private final String _id = IdGeneratorUtil.objectId();

    public BaseEntity() {
        this.id = this._id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return Objects.equals(this.id, this._id);
    }
}

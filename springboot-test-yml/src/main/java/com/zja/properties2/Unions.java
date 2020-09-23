package com.zja.properties2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "nrsc.unions")
public class Unions {
    private PartI partI = new PartI();
    private PartII partII = new PartII();
}

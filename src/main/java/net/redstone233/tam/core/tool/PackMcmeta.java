package net.redstone233.tam.core.tool;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record PackMcmeta(
        PackInfo pack,
        TamConfig tam
) {
    public record PackInfo(
            int packFormat,
            String description
    ) {
        public static final Codec<PackInfo> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("pack_format").forGetter(PackInfo::packFormat),
                        Codec.STRING.fieldOf("description").forGetter(PackInfo::description)
                ).apply(instance, PackInfo::new)
        );

        /**
         * 使用 DynamicOps 解析 PackInfo
         */
        public static <T> DataResult<PackInfo> parse(DynamicOps<T> ops, T input) {
            return CODEC.parse(ops, input);
        }
    }

    public static final Codec<PackMcmeta> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    PackInfo.CODEC.fieldOf("pack").forGetter(PackMcmeta::pack),
                    TamConfig.CODEC.fieldOf("tam").forGetter(PackMcmeta::tam)
            ).apply(instance, PackMcmeta::new)
    );

    /**
     * 使用 DynamicOps 解析完整配置
     */
    public static <T> DataResult<PackMcmeta> parse(DynamicOps<T> ops, T input) {
        return CODEC.parse(ops, input);
    }

    /**
     * 使用 DynamicOps 编码配置
     */
    public <T> DataResult<T> encode(DynamicOps<T> ops) {
        return CODEC.encodeStart(ops, this);
    }
}
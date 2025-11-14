package net.redstone233.tam.core.tool;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.dynamic.Codecs;
import java.util.List;
import java.util.Optional;

public record TamConfig(
        Text description,
        Optional<Integer> tamFormat,
        Optional<SupportedFormats> supportedFormats,
        Optional<Integer> maxFormat,
        Optional<Integer> minFormat
) {

    public record SupportedFormats(
            Optional<Integer> maxInclusive,
            Optional<Integer> minInclusive,
            Optional<List<Integer>> formatList,
            Optional<Integer> singleFormat
    ) {
        public static final Codec<SupportedFormats> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.optionalFieldOf("max_inclusive").forGetter(SupportedFormats::maxInclusive),
                        Codec.INT.optionalFieldOf("min_inclusive").forGetter(SupportedFormats::minInclusive),
                        Codec.INT.listOf().optionalFieldOf("supported_formats").forGetter(SupportedFormats::formatList),
                        Codec.INT.optionalFieldOf("supported_formats").forGetter(SupportedFormats::singleFormat)
                ).apply(instance, SupportedFormats::new)
        );

        public boolean isValidFormat(int format) {
            if (singleFormat.isPresent() && singleFormat.get() == format) {
                return true;
            }

            if (formatList.isPresent() && formatList.get().contains(format)) {
                return true;
            }

            if (minInclusive.isPresent() && maxInclusive.isPresent()) {
                return format >= minInclusive.get() && format <= maxInclusive.get();
            }

            return false;
        }
    }

    public static final Codec<TamConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    TextCodecs.CODEC.optionalFieldOf("description", Text.empty())
                            .forGetter(TamConfig::description),
                    Codec.INT.optionalFieldOf("tam_format").forGetter(TamConfig::tamFormat),
                    SupportedFormats.CODEC.optionalFieldOf("supported_formats").forGetter(TamConfig::supportedFormats),
                    Codec.INT.optionalFieldOf("max_format").forGetter(TamConfig::maxFormat),
                    Codec.INT.optionalFieldOf("min_format").forGetter(TamConfig::minFormat)
            ).apply(instance, TamConfig::new)
    );

    /**
     * 使用 DynamicOps 解析配置
     */
    public static <T> DataResult<TamConfig> parse(DynamicOps<T> ops, T input) {
        return CODEC.parse(ops, input);
    }

    /**
     * 使用 DynamicOps 编码配置
     */
    public <T> DataResult<T> encode(DynamicOps<T> ops) {
        return CODEC.encodeStart(ops, this);
    }

    public boolean isValidPackFormat(int packFormat) {
        if (maxFormat.isPresent() && minFormat.isPresent()) {
            return packFormat >= minFormat.get() && packFormat <= maxFormat.get();
        }

        return tamFormat.map(integer -> packFormat == integer).orElseGet(() -> supportedFormats.map(formats -> formats.isValidFormat(packFormat)).orElse(true));

    }

    public ConfigVersion getConfigVersion() {
        if (maxFormat.isPresent() || minFormat.isPresent()) {
            return ConfigVersion.V2;
        }
        return ConfigVersion.V1;
    }

    public enum ConfigVersion {
        V1, V2
    }
}
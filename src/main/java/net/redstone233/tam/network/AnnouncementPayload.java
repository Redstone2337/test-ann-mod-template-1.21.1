package net.redstone233.tam.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;
import net.redstone233.tam.config.AnnouncementConfig;

import java.util.ArrayList;
import java.util.List;

public record AnnouncementPayload(AnnouncementConfig config) implements CustomPayload {
    public static final CustomPayload.Id<AnnouncementPayload> ID =
            new CustomPayload.Id<>(Identifier.of(TestAnnMod.MOD_ID, "announcement"));

    public static final PacketCodec<RegistryByteBuf, AnnouncementPayload> CODEC =
            PacketCodec.ofStatic(AnnouncementPayload::write, AnnouncementPayload::read);

    public static AnnouncementPayload read(RegistryByteBuf buf) {
        AnnouncementConfig config = new AnnouncementConfig();

        // 读取字符串字段
        config.mainTitle = buf.readString();
        config.subTitle = buf.readString();

        // 读取公告内容列表
        int contentSize = buf.readInt();
        config.announcementContent = new ArrayList<>();
        for (int i = 0; i < contentSize; i++) {
            config.announcementContent.add(buf.readString());
        }

        config.confirmButtonText = buf.readString();
        config.submitButtonText = buf.readString();
        config.buttonLink = buf.readString();
        config.showIcon = buf.readBoolean();
        config.iconPath = buf.readString();
        config.iconWidth = buf.readInt();
        config.iconHeight = buf.readInt();
        config.iconTextSpacing = buf.readInt();
        config.useCustomRGB = buf.readBoolean();
        config.mainTitleColor = buf.readInt();
        config.subTitleColor = buf.readInt();
        config.contentColor = buf.readInt();
        config.scrollSpeed = buf.readDouble();
        config.useCustomAnnouncementBackground = buf.readBoolean();
        config.announcementBackgroundPath = buf.readString();
        config.showPonderScreen = buf.readBoolean();
        config.brewingEnabled = buf.readBoolean();
        config.brewingRecipeToDatapack = buf.readBoolean();

        return new AnnouncementPayload(config);
    }

    public static void write(RegistryByteBuf buf, AnnouncementPayload payload) {
        AnnouncementConfig config = payload.config;

        // 写入字符串字段
        buf.writeString(config.mainTitle != null ? config.mainTitle : "");
        buf.writeString(config.subTitle != null ? config.subTitle : "");

        // 写入公告内容列表
        List<String> content = config.announcementContent != null ? config.announcementContent : new ArrayList<>();
        buf.writeInt(content.size());
        for (String line : content) {
            buf.writeString(line != null ? line : "");
        }

        buf.writeString(config.confirmButtonText != null ? config.confirmButtonText : "");
        buf.writeString(config.submitButtonText != null ? config.submitButtonText : "");
        buf.writeString(config.buttonLink != null ? config.buttonLink : "");
        buf.writeBoolean(config.showIcon);
        buf.writeString(config.iconPath != null ? config.iconPath : "");
        buf.writeInt(config.iconWidth);
        buf.writeInt(config.iconHeight);
        buf.writeInt(config.iconTextSpacing);
        buf.writeBoolean(config.useCustomRGB);
        buf.writeInt(config.mainTitleColor);
        buf.writeInt(config.subTitleColor);
        buf.writeInt(config.contentColor);
        buf.writeDouble(config.scrollSpeed);
        buf.writeBoolean(config.useCustomAnnouncementBackground);
        buf.writeString(config.announcementBackgroundPath != null ? config.announcementBackgroundPath : "");
        buf.writeBoolean(config.showPonderScreen);
        buf.writeBoolean(config.brewingEnabled);
        buf.writeBoolean(config.brewingRecipeToDatapack);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
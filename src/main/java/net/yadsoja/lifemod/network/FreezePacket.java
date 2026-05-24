package net.yadsoja.lifemod.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record FreezePacket(boolean frozen) implements CustomPayload {

    public static final Id<FreezePacket> ID =
            new Id<>(Identifier.of("lifemod", "freeze_sync"));

    public static final PacketCodec<PacketByteBuf, FreezePacket> CODEC =
            PacketCodec.of(
                    (value, buf) -> buf.writeBoolean(value.frozen()),
                    buf -> new FreezePacket(buf.readBoolean())
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
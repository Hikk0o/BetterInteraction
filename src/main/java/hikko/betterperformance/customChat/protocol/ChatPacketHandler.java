package hikko.betterperformance.customChat.protocol;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import hikko.betterperformance.BetterPerformance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ChatPacketHandler {

	public ChatPacketHandler() {

		BetterPerformance.getProtocolManager().addPacketListener(new PacketAdapter(BetterPerformance.getInstance(),
				ListenerPriority.NORMAL,
				PacketType.Play.Server.CHAT) {
			@Override
			public void onPacketSending(PacketEvent event) {
				if (event.getPacketType() != PacketType.Play.Server.CHAT) return;

				PacketContainer packet = event.getPacket();
				Player player = event.getPlayer();

				try {
					StructureModifier<WrappedChatComponent> chatComponents = packet.getChatComponents();
					for (WrappedChatComponent component : chatComponents.getValues()) {

						String json = component.getJson();
						Component component1 = GsonComponentSerializer.gson().deserialize(json);
						BetterPerformance.getInstance().getLogger().log(Level.INFO, "To " + player.getName() + ": " + PlainTextComponentSerializer.plainText().serialize(component1));
					}
				}
				catch (NullPointerException e) {
					e.getCause();
				}

			}
		});
	}
}

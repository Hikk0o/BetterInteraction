package hikko.betterinteraction.customChat.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import hikko.betterinteraction.BetterInteraction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;

public class ChatPacketHandler {

	public ChatPacketHandler() {
		BetterInteraction.getProtocolManager().addPacketListener(new PacketAdapter(BetterInteraction.getInstance(),
				ListenerPriority.NORMAL,
				PacketType.Play.Server.CHAT) {
			@Override
			public void onPacketSending(PacketEvent event) {
				if (event.getPacketType() != PacketType.Play.Server.CHAT) return;

				PacketContainer packet = event.getPacket();
				Player player = event.getPlayer();

				try {

					if (packet.getChatTypes().getValues().isEmpty()) return;
					if (packet.getChatTypes().getValues().get(0) == EnumWrappers.ChatType.GAME_INFO) return;
					if (player == null) return;
					if (BetterInteraction.getInstance().getChatEvents().getMessageQueue().getPlayer(player) == null) return;
					if (BetterInteraction.getInstance().getChatEvents().getMessageQueue().getPlayer(player).isLock()) return;

					StructureModifier<WrappedChatComponent> chatComponents = packet.getChatComponents();
					Component component;
					try {
						component = GsonComponentSerializer.gson().deserialize(chatComponents.getValues().get(0).getJson());
					} catch (NullPointerException e) {
//						BetterPerformance.getInstance().getLogger().log(Level.WARNING, " NullPointerException ChatPacketHandler");
						return;
					}
					BetterInteraction.getInstance().getChatEvents().getMessageQueue().getPlayer(player).addMessage(component);

//					BetterPerformance.getInstance().getLogger().log(Level.INFO, "To " + player.getName() + ": " + PlainTextComponentSerializer.plainText().serialize(component));
//					BetterPerformance.getInstance().getLogger().log(Level.INFO, "To " + player.getName() + ": " + packet.getChatTypes().getValues());
				}
				catch (NullPointerException e) {
					e.printStackTrace();
				}

			}
		});
	}
}

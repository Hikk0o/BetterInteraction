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
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ChatPacketHandler {

	public ChatPacketHandler() {
		BetterPerformance.getInstance().getLogger().log(Level.INFO, "Loading ChatPacketHandler...");

		BetterPerformance.getProtocolManager().addPacketListener(new PacketAdapter(BetterPerformance.getInstance(),
				ListenerPriority.NORMAL,
				PacketType.Play.Server.CHAT) {
			@Override
			public void onPacketSending(PacketEvent event) {
				if (event.getPacketType() != PacketType.Play.Server.CHAT) return;

				PacketContainer packet = event.getPacket();
				Player player = event.getPlayer();

				try {

					if (packet.getChatTypes().getValues().get(0) == EnumWrappers.ChatType.GAME_INFO) return;
					if (BetterPerformance.getInstance().getChatEvents().getMessageQueue().getPlayer(player).isLock()) return;

					StructureModifier<WrappedChatComponent> chatComponents = packet.getChatComponents();
					Component component;
					try {
						component = GsonComponentSerializer.gson().deserialize(chatComponents.getValues().get(0).getJson());
					} catch (NullPointerException e) {
//						BetterPerformance.getInstance().getLogger().log(Level.WARNING, " NullPointerException ChatPacketHandler");
						return;
					}


					BetterPerformance.getInstance().getChatEvents().getMessageQueue().getPlayer(player).addMessage(component);

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

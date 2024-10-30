package com.fullskele.eruditetweaks.tweaks.corpsecomplex;

import com.fullskele.eruditetweaks.ConfigHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import c4.corpsecomplex.common.modules.spawning.capability.DeathLocation;
import c4.corpsecomplex.common.modules.spawning.capability.IDeathLocation;

public class CommandDeathScrollClear extends CommandBase {

    @Override
    public String getName() {
        return "deathscrollclear";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/deathscrollclear <player>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new CommandException("Usage: /deathscrollclear <player>");
        }

        EntityPlayerMP player = getPlayer(server, sender, args[0]);

        if (player == null) {
            throw new CommandException("Error: Player not found");
        }

        clearPlayerDeathScroll(player);

        if (ConfigHandler.SHOW_DEATH_SCROLL_CLEAR_MESSAGE) sender.sendMessage(new TextComponentString("Cleared death scroll for player: " + player.getName()));
    }

    private void clearPlayerDeathScroll(EntityPlayerMP player) {
        if (player.world.isRemote) return;
        IDeathLocation deathLoc = player.getCapability(DeathLocation.Provider.DEATH_LOC_CAP, null);
        deathLoc.setUsedScroll(true);
    }
}

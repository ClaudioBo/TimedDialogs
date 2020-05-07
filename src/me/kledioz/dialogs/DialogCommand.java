package me.kledioz.dialogs;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.kledioz.dialogs.utils.TitleAPI;

public class DialogCommand implements CommandExecutor {

	Main main;

	public DialogCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			if (args.length == 0) {
				sender.sendMessage("§eTimedDialogs commands:");
				sender.sendMessage(" §r- /" + label + " start <Dialog Name> <Player Name>");
				sender.sendMessage(" §r- /" + label + " stop <Player Name>");
				sender.sendMessage(" §r- /" + label + " reload");
			} else {
				if (args[0].equalsIgnoreCase("start")) {
					if (args.length == 3) {
						String dialog = args[1].toLowerCase();
						String player = args[2];
						if (main.dialogos.containsKey(dialog)) {
							Player sendTo = Bukkit.getPlayer(player);
							if (sendTo != null && sendTo.isOnline()) {
								BukkitTask i = new BukkitRunnable() {
									Player p = sendTo;
									List<String> script = main.dialogos.get(dialog);
									int currentPos = 0;
									int holdSecs = 0;

									@Override
									public void run() {
										if (holdSecs <= 0) {
											for (; currentPos < script.size(); currentPos++) {
												String line = script.get(currentPos);
												String[] lineSplitted = line.split(" ");
												switch (lineSplitted[0].toLowerCase()) {
												case "MESSAGE":
													String message = line.replaceFirst(lineSplitted[0] + " ", "");
//													for (int i = 1; i < lineSplitted.length; i++) {
//														if (lineSplitted.length < i) {
//															message += lineSplitted[i] + " ";
//														} else {
//															message += lineSplitted[i];
//														}
//													}
													p.sendMessage(message.replace("&", "§"));
													break;
												case "COMMAND":
													String command = line.replaceFirst(lineSplitted[0] + " " + lineSplitted[1] + " ", "");
													if (lineSplitted[1].equalsIgnoreCase("AS_SERVER")) {
														Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", p.getName()));
													} else if (lineSplitted[1].equalsIgnoreCase("AS_PLAYER")) {
														Bukkit.dispatchCommand(p, command.replace("{player}", p.getName()));
													}
													break;
												case "SOUND":
													Sound snd = null;
													float pitch = 0f;

													// WTF xd
													try {
														try {
															snd = Sound.valueOf(lineSplitted[1].toUpperCase());
														} catch (NumberFormatException e) {
															main.getLogger().log(Level.WARNING, String.format("Segundos invalidos, linea #%s (%s)", currentPos, line));
														} catch (ArrayIndexOutOfBoundsException e) {
															main.getLogger().log(Level.WARNING, String.format("Faltan argumentos, linea #%s (%s)", currentPos, line));
														} catch (Exception e) {
															main.getLogger().log(Level.WARNING, String.format("Error, linea #%s (%s)", currentPos, line));
														}

														try {
															pitch = Float.valueOf(lineSplitted[2]);
														} catch (NumberFormatException e) {
															main.getLogger().log(Level.WARNING, String.format("Segundos invalidos, linea #%s (%s)", currentPos, line));
														} catch (ArrayIndexOutOfBoundsException e) {
															main.getLogger().log(Level.WARNING, String.format("Faltan argumentos, linea #%s (%s)", currentPos, line));
														} catch (Exception e) {
															main.getLogger().log(Level.WARNING, String.format("Error, linea #%s (%s)", currentPos, line));
														}
													} catch (Exception e) {
														break;
													}

													p.playSound(p.getLocation(), snd, 1f, pitch);
													break;
												case "TITLE":
													int fadeIn = 0;
													int stay = 0;
													int fadeOut = 0;
													String[] text = line.replaceFirst(lineSplitted[0] + " " + lineSplitted[1] + " " + lineSplitted[2] + " " + lineSplitted[3] + " ", "").split(";");
													// WTF xd
													try {
														try {
															fadeIn = Integer.valueOf(lineSplitted[1]);
														} catch (NumberFormatException e) {
															main.getLogger().log(Level.WARNING, String.format("Segundos invalidos, linea #%s (%s)", currentPos, line));
														} catch (ArrayIndexOutOfBoundsException e) {
															main.getLogger().log(Level.WARNING, String.format("Faltan argumentos, linea #%s (%s)", currentPos, line));
														} catch (Exception e) {
															main.getLogger().log(Level.WARNING, String.format("Error, linea #%s (%s)", currentPos, line));
														}

														try {
															stay = Integer.valueOf(lineSplitted[2]);
														} catch (NumberFormatException e) {
															main.getLogger().log(Level.WARNING, String.format("Segundos invalidos, linea #%s (%s)", currentPos, line));
														} catch (ArrayIndexOutOfBoundsException e) {
															main.getLogger().log(Level.WARNING, String.format("Faltan argumentos, linea #%s (%s)", currentPos, line));
														} catch (Exception e) {
															main.getLogger().log(Level.WARNING, String.format("Error, linea #%s (%s)", currentPos, line));
														}

														try {
															fadeOut = Integer.valueOf(lineSplitted[3]);
														} catch (NumberFormatException e) {
															main.getLogger().log(Level.WARNING, String.format("Segundos invalidos, linea #%s (%s)", currentPos, line));
														} catch (ArrayIndexOutOfBoundsException e) {
															main.getLogger().log(Level.WARNING, String.format("Faltan argumentos, linea #%s (%s)", currentPos, line));
														} catch (Exception e) {
															main.getLogger().log(Level.WARNING, String.format("Error, linea #%s (%s)", currentPos, line));
														}

													} catch (Exception e) {
														break;
													}

													TitleAPI.sendTitle(p, fadeIn, stay, fadeOut, text[0], text[1]);

													break;
												case "WAIT":
													try {
														holdSecs = Integer.parseInt(lineSplitted[1]);
													} catch (NumberFormatException e) {
														main.getLogger().log(Level.WARNING, String.format("Segundos invalidos, linea #%s (%s)", currentPos, line));
													} catch (ArrayIndexOutOfBoundsException e) {
														main.getLogger().log(Level.WARNING, String.format("Faltan argumentos, linea #%s (%s)", currentPos, line));
													} catch (Exception e) {
														main.getLogger().log(Level.WARNING, String.format("Error, linea #%s (%s)", currentPos, line));
													}
													return;
												default:
													main.getLogger().log(Level.WARNING, String.format("Instruccion #%s '%s' invalida. (%s)", currentPos, lineSplitted[0].toUpperCase(), line));
													break;
												}
											}
											if (currentPos >= script.size()) {
												this.cancel();
												main.tasks.remove(getTaskId());
											}
										} else {
											holdSecs--;
										}
									}
								}.runTaskTimer(main, 0l, 20L);
								main.tasks.put(i.getTaskId(), i);

							}
						}
					} else {

					}
				}
			}
		} else {
			sender.sendMessage("§cNo tienes permisos.");
		}
		return false;
	}

}

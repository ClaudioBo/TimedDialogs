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

		// Solo consola o OPS
		if ((sender instanceof Player && !sender.isOp())) {
			sender.sendMessage("§cNo tienes permisos.");
			return false;
		}

		if (args.length == 0) {
			sender.sendMessage("§eTimedDialogs commands:");
			sender.sendMessage(" §r- /" + label + " start <Dialogo> <Usuario>");
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
											switch (lineSplitted[0].toUpperCase()) {
											case "MESSAGE":
												String message = line.replaceFirst(lineSplitted[0] + " ", "");
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
												float volume = 1f;
												float pitch = 1f;

												// WTF xd
												try {
													try {
														snd = Sound.valueOf(lineSplitted[1].toUpperCase());
													} catch (NumberFormatException e) {
														main.getLogger().log(Level.WARNING, String.format("Sonido invalido, linea #%s (%s)", currentPos, line));
													} catch (ArrayIndexOutOfBoundsException e) {
														main.getLogger().log(Level.WARNING, String.format("Faltan argumentos, linea #%s (%s)", currentPos, line));
													} catch (Exception e) {
														main.getLogger().log(Level.WARNING, String.format("Error, linea #%s (%s)", currentPos, line));
													}

													try {
														volume = Float.valueOf(lineSplitted[1]);
													} catch (NumberFormatException e) {
														main.getLogger().log(Level.WARNING, String.format("Volumen invalido, linea #%s (%s)", currentPos, line));
													} catch (ArrayIndexOutOfBoundsException e) {
														main.getLogger().log(Level.WARNING, String.format("Faltan argumentos, linea #%s (%s)", currentPos, line));
													} catch (Exception e) {
														main.getLogger().log(Level.WARNING, String.format("Error, linea #%s (%s)", currentPos, line));
													}

													try {
														pitch = Float.valueOf(lineSplitted[2]);
													} catch (NumberFormatException e) {
														main.getLogger().log(Level.WARNING, String.format("Tono invalido, linea #%s (%s)", currentPos, line));
													} catch (ArrayIndexOutOfBoundsException e) {
														main.getLogger().log(Level.WARNING, String.format("Faltan argumentos, linea #%s (%s)", currentPos, line));
													} catch (Exception e) {
														main.getLogger().log(Level.WARNING, String.format("Error, linea #%s (%s)", currentPos, line));
													}
												} catch (Exception e) {
													break;
												}

												p.playSound(p.getLocation(), snd, volume, pitch);
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
												currentPos++;
												return;
											default:
												main.getLogger().log(Level.WARNING, String.format("Instruccion #%s '%s' invalida. (%s)", currentPos, lineSplitted[0].toUpperCase(), line));
												break;
											}
										}
										if (currentPos >= script.size()) {
											this.cancel();
											main.tasks.remove(p);
										}
									} else {
										holdSecs--;
									}
								}
							}.runTaskTimer(main, 0l, 20L);
							main.tasks.put(sendTo, i);

						} else {
							sender.sendMessage("§cUsuario no encontrado.");
						}
					} else {
						sender.sendMessage("§cDialogo no encontrado");
					}
				} else {
					sender.sendMessage("§cUso correcto: /" + label + " start <Dialogo> <Usuario>");
				}
			} else if (args[0].equalsIgnoreCase("stop")) {
				if (args.length == 2) {
					String player = args[1];
					Player stopTo = Bukkit.getPlayer(player);
					if (stopTo != null && stopTo.isOnline()) {
						if (main.tasks.containsKey(stopTo)) {
							main.tasks.get(stopTo).cancel();
							main.tasks.remove(stopTo);
						} else {
							sender.sendMessage("§cUsuario no anda en un dialogo");
						}
					} else {
						sender.sendMessage("§cUsuario no esta en linea");
					}
				} else {
					sender.sendMessage("§cUso correcto: /" + label + " stop <Usuario>");
				}
			} else if (args[0].equalsIgnoreCase("reload")) {
				main.reloadConfig();
				main.loadDialogs();
				sender.sendMessage("§aConfiguracion recargada, " + main.tasks.size() + " dialogos cancelados.");
				for (BukkitTask task : main.tasks.values()) {
					task.cancel();
				}
				main.tasks.clear();
			}
		}
		return false;
	}

}

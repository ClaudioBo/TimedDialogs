# TimedDialogs
Secuencia de `comandos`, ideal para crear dialogos, o cosas relacionadas.

## Comandos
> /tdialogs

Muestra los comandos disponibles.

> /tdialogs reload

Recarga la configuracion, registrando de nuevo los dialogos, y cancelando los dialogos que se esten ejecutando en el momento.

> /tdialogs start <Dialogo> <Jugador>

Se ejecutara el dialogo secuencialmente hacia un jugador especificado.
  
> /tdialogs stop <Jugador>

Se detiene la ejecucion de un dialogo que este un jugador especificado.

## Configuracion
### config.yml
```yaml
dialogs:
  bienvenida:
  - 'TITLE 20 80 20 &6&lNUEVA CAMPAÑA;El comienzo'
  - 'MESSAGE '
  - 'MESSAGE   &6&lHAZ COMENZADO UNA NUEVA CAMPAÑA'
  - 'MESSAGE   &fEsto es solo el comienzo'
  - 'MESSAGE '
  - 'WAIT 100'
  - 'SOUND VILLAGER_IDLE 1.0 1.0'
  - 'MESSAGE &6[Juanito] &a¡Hola!'
  conseguirCobblestone:
  - 'SOUND VILLAGER_IDLE 1.0 1.0'
  - 'MESSAGE &6[Juanito] &aQue onda, necesito que me hagas un favor'
  - 'WAIT 20'
  - 'SOUND VILLAGER_IDLE 1.0 1.0'
  - 'MESSAGE &6[Juanito] &aEn estos momentos estoy juntando recursos, para construir una casa'
  - 'WAIT 20'
  - 'SOUND VILLAGER_IDLE 1.0 1.0'
  - 'MESSAGE &6[Juanito] &aNecesito que me consigas &e36 bloques de Cobblestone'
  - 'WAIT 20'
  - 'SOUND VILLAGER_IDLE 1.0 1.0'
  - 'MESSAGE &6[Juanito] &aVe por ellos, porfavor.'
```
`dialogs` es la contenedora de todos los 'dialogos'
Dentro de `dialogs` se pueden agregar todos los que se desee, como por ejemplo `bienvenida` y `conseguirCobblestone`
Cada dialogo, tendra una lista de *instrucciones* que sera ejecutadas secuencialmente.

- **Instrucciones disponibles**
  - TITLE \<fadeInTicks\> \<stayTicks\> \<fadeOutTicks\> \<message...\>
  - MESSAGE <message...>
  - COMMAND \<AS_SERVER/AS_PLAYER\> <command {player}...>
  - SOUND \<bukkitEnum\> <volume 0.0 to 2.0> <pitch 0.5 to 2.0>
  - WAIT \<20 ticks = 1 second\>


---
Me quedo bonito el README.md 💕

package nl.t64.game.rpg.screens.world;

import nl.t64.game.rpg.components.character.Character;
import nl.t64.game.rpg.components.character.*;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.events.character.StartDirectionEvent;
import nl.t64.game.rpg.events.character.StartPositionEvent;
import nl.t64.game.rpg.events.character.StartStateEvent;

import java.util.ArrayList;
import java.util.List;


class NpcCharactersLoader {

    private final GameMap currentMap;
    private final List<Character> npcCharacters;

    NpcCharactersLoader(GameMap currentMap) {
        this.currentMap = currentMap;
        this.npcCharacters = new ArrayList<>();
    }

    List<Character> createNpcs() {
        loadNpcs();
        loadHeroes();
        return npcCharacters;
    }

    private void loadNpcs() {
        for (GameMapNpc gameMapNpc : currentMap.npcs) {
            String spriteId = gameMapNpc.name;
            var npcCharacter = new Character(new InputNpc(), new PhysicsNpc(), new GraphicsNpc(spriteId));
            loadNpcCharacter(gameMapNpc, npcCharacter);
        }
    }

    private void loadHeroes() {
        for (GameMapHero gameMapHero : currentMap.heroes) {
            String spriteId = gameMapHero.name;
            var heroCharacter = new Character(new InputNpc(), new PhysicsHero(spriteId), new GraphicsNpc(spriteId));
            loadNpcCharacter(gameMapHero, heroCharacter);
        }
    }

    private void loadNpcCharacter(GameMapNpc gameMapNpc, Character npcCharacter) {
        npcCharacters.add(npcCharacter);
        npcCharacter.send(new StartStateEvent(gameMapNpc.state));
        npcCharacter.send(new StartDirectionEvent(gameMapNpc.direction));
        npcCharacter.send(new StartPositionEvent(gameMapNpc.getPosition()));
        if (gameMapNpc.state.equals(CharacterState.IMMOBILE)) {
            currentMap.addToBlockers(npcCharacter.getBoundingBox());
        }
    }

}

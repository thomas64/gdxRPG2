package nl.t64.game.rpg.screens.world;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.character.Character;
import nl.t64.game.rpg.components.character.*;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;

import java.util.ArrayList;
import java.util.Collections;
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
        return Collections.unmodifiableList(npcCharacters);
    }

    private void loadNpcs() {
        for (GameMapNpc gameMapNpc : currentMap.npcs) {
            loadNpcCharacter(gameMapNpc);
        }
    }

    private void loadHeroes() {
        for (GameMapHero gameMapHero : currentMap.heroes) {
            HeroItem hero = Utils.getGameData().getHeroes().getHero(gameMapHero.name);
            if (gameMapHero.hasBeenRecruited == hero.isHasBeenRecruited()) {
                loadNpcCharacter(gameMapHero);
            }
        }
    }

    private void loadNpcCharacter(GameMapNpc gameMapNpc) {
        String characterId = gameMapNpc.name;
        var npcCharacter = new Character(characterId, new InputNpc(), new PhysicsNpc(), new GraphicsNpc(characterId));
        npcCharacters.add(npcCharacter);
        npcCharacter.send(new LoadCharacterEvent(gameMapNpc.state,
                                                 gameMapNpc.direction,
                                                 gameMapNpc.getPosition(),
                                                 gameMapNpc.conversation));
        if (gameMapNpc.state.equals(CharacterState.IMMOBILE)
            || gameMapNpc.state.equals(CharacterState.FLOATING)) {
            currentMap.addToBlockers(npcCharacter.getBoundingBox());
        }
    }

}

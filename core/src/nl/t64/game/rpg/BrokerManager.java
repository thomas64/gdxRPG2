package nl.t64.game.rpg;

import nl.t64.game.rpg.subjects.*;


public class BrokerManager {

    public final ActionSubject actionObservers;
    public final BlockSubject blockObservers;
    public final BumpSubject bumpObservers;
    public final DetectionSubject detectionObservers;
    public final CollisionSubject collisionObservers;

    public final ComponentSubject componentObservers;
    public final LootSubject lootObservers;
    public final MapSubject mapObservers;

    public final PartySubject partyObservers;
    public final ProfileSubject profileObservers;

    BrokerManager() {
        this.actionObservers = new ActionSubject();
        this.blockObservers = new BlockSubject();
        this.bumpObservers = new BumpSubject();
        this.detectionObservers = new DetectionSubject();
        this.collisionObservers = new CollisionSubject();

        this.componentObservers = new ComponentSubject();
        this.lootObservers = new LootSubject();
        this.mapObservers = new MapSubject();

        this.partyObservers = new PartySubject();
        this.profileObservers = new ProfileSubject();
    }

}

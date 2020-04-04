package eatyourbeets.monsters.Bosses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.Bosses.KrulTepesMoveset.*;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.relics.animator.ExquisiteBloodVial;

public class KrulTepes extends EYBMonster
{
    public static final String ID = CreateFullID(KrulTepes.class);

    private final BobEffect bobEffect = new BobEffect(1);

    public KrulTepes()
    {
        super(new Data(ID), EnemyType.BOSS);

        moveset.mode = EYBMoveset.Mode.Sequential;

        moveset.AddNormal(new Move_Regenerate());
        moveset.AddNormal(new Move_Bite());
        moveset.AddNormal(new Move_PowerUp());
        moveset.AddNormal(new Move_Cripple());
        moveset.AddNormal(new Move_MultiSlash());
        moveset.AddNormal(new Move_GuardedAttack());
    }

    @Override
    public void render(SpriteBatch sb)
    {
        animY = this.bobEffect.y;
        super.render(sb);
    }

    @Override
    public void update()
    {
        this.bobEffect.update();
        super.update();
    }

    @Override
    public void die()
    {
        super.die();

        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        playBossStinger();

        ExquisiteBloodVial exquisiteBloodVial = (ExquisiteBloodVial)AbstractDungeon.player.getRelic(ExquisiteBloodVial.ID);
        if (exquisiteBloodVial != null)
        {
            exquisiteBloodVial.UnlockPotential();
        }
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.StackPower(new IntangiblePlayerPower(this, 1));
        GameActions.Bottom.StackPower(new ArtifactPower(this, 2));

        AbstractPlayer p = AbstractDungeon.player;
        p.drawPile.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.series.OwariNoSeraph.KrulTepes);
        p.discardPile.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.series.OwariNoSeraph.KrulTepes);
        p.hand.group.removeIf(c -> c instanceof eatyourbeets.cards.animator.series.OwariNoSeraph.KrulTepes);

        AbstractDungeon.getCurrMapNode().room.playBGM("BOSS_BOTTOM");
    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            maxHealth = AbstractDungeon.ascensionLevel >= 6 ? 377 : 344;
            imgUrl = AnimatorResources.GetMonsterImage(ID);

            SetHB(0,0,200,280);
        }
    }
}
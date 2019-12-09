package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.actions._legacy.animator.PlayTempBgmAction;
import eatyourbeets.blights.animator.Doomed;
import eatyourbeets.cards.animator.Respite;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.AbstractMove;

public class Move_Fading extends AbstractMove
{
    private boolean firstTime;

    public int fadingTurns;

    public Move_Fading(int turns)
    {
        fadingTurns = turns;
        firstTime = true;
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        return super.CanUse(previousMove) && !AbstractDungeon.player.hasBlight(Doomed.ID);
                //!AbstractDungeon.player.hasPower(GenericFadingPower.POWER_ID);
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        GameActions.Bottom.SFX("MONSTER_COLLECTOR_DEBUFF");
        GameActions.Bottom.VFX(new CollectorCurseEffect(target.hb.cX, target.hb.cY), 2.0F);
        GameActionsHelper_Legacy.Callback(new WaitAction(0.1f),
                (state, action) -> AbstractDungeon.getCurrRoom().spawnBlightAndObtain(owner.hb.cX, owner.hb.cY, new Doomed(fadingTurns)), this);

//        GenericFadingPower fading = (GenericFadingPower) target.getPower(GenericFadingPower.POWER_ID);
//
//        if (fading != null)
//        {
//            fading.amount = fadingTurns;
//        }
//        else
//        {
//            fading = new GenericFadingPower(target, fadingTurns);
//            target.powers.add(fading);
//        }
//
//        fadingTurns = Math.max(1, fadingTurns / 2);
//
//        fading.flash();
//        if (owner instanceof TheUnnamed)
//        {
//            ((TheUnnamed) owner).appliedFading = true;
//        }

        if (firstTime)
        {
            GameActionsHelper_Legacy.AddToBottom(new PlayTempBgmAction("MINDBLOOM", 1));

            target.drawPile.addToTop(new Respite());

            firstTime = false;
        }
    }
}
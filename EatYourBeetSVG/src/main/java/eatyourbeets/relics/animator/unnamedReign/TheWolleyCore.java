package eatyourbeets.relics.animator.unnamedReign;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class TheWolleyCore extends UnnamedReignRelic
{
    public static final String ID = AnimatorRelic.CreateFullID(TheWolleyCore.class.getSimpleName());

    private static final int CARD_DRAW = 2;
    private static final int DAMAGE_AMOUNT = 2;
    private static final int BLOCK_AMOUNT = 1;

    public TheWolleyCore()
    {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], CARD_DRAW, DAMAGE_AMOUNT, BLOCK_AMOUNT);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        GameActions.Bottom.Draw(CARD_DRAW);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        AbstractPlayer p = AbstractDungeon.player;

        GameActions.Bottom.SFX("ATTACK_HEAVY");
        if (Settings.FAST_MODE)
        {
            GameActions.Bottom.VFX(new CleaveEffect());
        }
        else
        {
            GameActions.Bottom.VFX(new CleaveEffect(), 0.2f);
        }

        GameActions.Bottom.Add(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(DAMAGE_AMOUNT, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE, true));
        GameActions.Bottom.Add(new GainBlockAction(p, p, BLOCK_AMOUNT, true));
    }

    @Override
    protected void OnManualEquip()
    {

    }
}
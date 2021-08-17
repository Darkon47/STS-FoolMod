package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Enchantment1 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment1.class);
    public static final int INDEX = 1;
    public static final int UP1_GAIN_BLOCK = 7;
    public static final int UP3_TAKE_DAMAGE = 3;

    private static final CardEffectChoice choices = new CardEffectChoice();
    private static final float D_X = CardGroup.DRAW_PILE_X * 1.5f;
    private static final float D_Y = CardGroup.DRAW_PILE_Y * 3.5f;

    public Enchantment1()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 0, 2);
    }

    @Override
    protected String GetRawDescription()
    {
        return upgradeIndex == 1 ? super.GetRawDescription(UP1_GAIN_BLOCK)
             : upgradeIndex == 3 ? super.GetRawDescription(UP3_TAKE_DAMAGE)
             : super.GetRawDescription();
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 3;
    }

    @Override
    protected void OnUpgrade()
    {
        if (upgradeIndex == 1)
        {
            upgradeMagicNumber(UP1_GAIN_BLOCK);
        }
        else if (upgradeIndex == 3)
        {
            upgradeSecondaryValue(-1);
            upgradeMagicNumber(UP3_TAKE_DAMAGE);
        }
    }

    @Override
    public boolean CanUsePower(int cost)
    {
        return super.CanUsePower(cost) && (upgradeIndex != 3 || (GameUtilities.GetHP(player, true, false) > magicNumber));
    }

    @Override
    public void PayPowerCost(int cost)
    {
        super.PayPowerCost(cost);

        if (upgradeIndex == 3)
        {
            GameActions.Bottom.TakeDamage(magicNumber, AttackEffects.NONE);
        }
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_EnterStance(ForceStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(AgilityStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(IntellectStance.STANCE_ID));
        }

        choices.Select(1, null)
        .AddCallback(player.stance.ID, (stance, cards) ->
        {
            if (upgradeIndex == 1 && cards.size() > 0 && cards.get(0).cardID.equals(stance))
            {
                GameActions.Bottom.GainBlock(magicNumber);
            }
        });

        if (upgradeIndex == 2)
        {
            GameActions.Bottom.SelectFromPile(name, 1, player.drawPile)
            .SetOptions(CardSelection.Bottom, false)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    GameActions.Bottom.Motivate(cards.get(0), 1)
                    .AddCallback(c -> GameEffects.List.ShowCardBriefly(c.makeStatEquivalentCopy(), D_X, D_Y));
                }
            });
        }
    }
}
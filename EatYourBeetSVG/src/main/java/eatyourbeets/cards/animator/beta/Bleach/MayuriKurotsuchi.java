package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class MayuriKurotsuchi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MayuriKurotsuchi.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Normal);

    public MayuriKurotsuchi()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 2, 1);
        SetEthereal(true);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        int force = GameUtilities.GetPowerAmount(AbstractDungeon.player, ForcePower.POWER_ID);
        int agility = GameUtilities.GetPowerAmount(AbstractDungeon.player, AgilityPower.POWER_ID);

        GameUtilities.IncreaseMagicNumber(this,  force + agility, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPoison(TargetHelper.Normal(m), magicNumber);

        int poisonThreshold = 30;

        if (GameUtilities.GetPowerAmount(m, PoisonPower.POWER_ID) >= poisonThreshold)
        {
            ApplyRandomCommonDebuff(m);
        }
    }

    private void ApplyRandomCommonDebuff(AbstractMonster m)
    {
        int random = GameUtilities.GetRNG().random(0, 3);

        switch (random)
        {
            case 0:
                //Apply Weak
                GameActions.Bottom.ApplyWeak(TargetHelper.Normal(m),secondaryValue);
                break;
            case 1:
                //Apply Vulnerable
                GameActions.Bottom.ApplyVulnerable(TargetHelper.Normal(m),secondaryValue);
                break;
            case 2:
                //Apply Burning
                GameActions.Bottom.ApplyBurning(TargetHelper.Normal(m),secondaryValue);
                break;
            default:
                //Apply Shackles
                GameActions.Bottom.ReduceStrength(m, secondaryValue, true);
                break;
        }
    }
}
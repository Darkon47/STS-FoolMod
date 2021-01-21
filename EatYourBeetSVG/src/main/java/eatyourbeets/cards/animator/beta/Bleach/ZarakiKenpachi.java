package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnBlockBrokenSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;

public class ZarakiKenpachi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ZarakiKenpachi.class).SetPower(2, CardRarity.RARE);

    public ZarakiKenpachi()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 2);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new ZarakiKenpachiPower(p, this.magicNumber));
    }

    public static class ZarakiKenpachiPower extends AnimatorPower implements OnBlockBrokenSubscriber
    {
        public ZarakiKenpachiPower(AbstractPlayer owner, int amount)
        {
            super(owner, ZarakiKenpachi.DATA);

            this.amount = amount;

            ForcePower.StartAlwaysPreserve();
            AgilityPower.StartDisable();
            IntellectPower.StartDisable();

            updateDescription();
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            ForcePower.StopAlwaysPreserve();
            AgilityPower.StopDisable();
            IntellectPower.StopDisable();
        }

        @Override
        public void OnBlockBroken(AbstractCreature creature)
        {
            if (!creature.isPlayer)
            {
                GameActions.Bottom.GainStrength(amount);
                GameActions.Bottom.ApplyPower(new LoseStrengthPower(player, amount));
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}
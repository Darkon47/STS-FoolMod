package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Zhongli extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zhongli.class).SetPower(3, CardRarity.RARE);

    public Zhongli()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);

        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new ZhongliPower(p, this.magicNumber, this.secondaryValue));
    }

    public static class ZhongliPower extends AnimatorPower
    {
        private final int secondaryAmount;

        public ZhongliPower(AbstractPlayer owner, int amount, int secondaryAmount)
        {
            super(owner, Zhongli.DATA);

            this.amount = amount;
            this.secondaryAmount = secondaryAmount;

            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            int energy = Math.min(amount, EnergyPanel.getCurrentEnergy());
            if (energy > 0)
            {
                EnergyPanel.useEnergy(energy);
                GameActions.Bottom.ChannelOrb(new Earth());
                GameActions.Bottom.GainPlatedArmor(amount);
            }
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb) {
            if (Earth.ORB_ID.equals(orb.ID)) {
                GameActions.Bottom.GainBlock(orb.evokeAmount);
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, 1, amount);
        }
    }
}
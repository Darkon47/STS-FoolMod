package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.DarknessPower;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class Darkness extends AnimatorCard
{
    public static final String ID = Register(Darkness.class.getSimpleName(), EYBCardBadge.Special);

    public Darkness()
    {
        super(ID, 1, CardType.POWER, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,2,2);

        SetSynergy(Synergies.Konosuba);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new DarknessAdrenaline(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.GainBlock(block);
        GameActionsHelper2.StackPower(new PlatedArmorPower(p, this.magicNumber));
        GameActionsHelper2.StackPower(new DarknessPower(p, 1));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(1);
            upgradeMagicNumber(1);
        }
    }
}
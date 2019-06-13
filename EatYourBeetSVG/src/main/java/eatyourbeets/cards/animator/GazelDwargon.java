package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.GazelDwargonPower;

public class GazelDwargon extends AnimatorCard
{
    public static final String ID = CreateFullID(GazelDwargon.class.getSimpleName());

    public GazelDwargon()
    {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 1);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new GazelDwargonPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}
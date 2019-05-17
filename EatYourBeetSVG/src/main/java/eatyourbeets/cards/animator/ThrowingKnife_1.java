package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.GameActionsHelper;

public class ThrowingKnife_1 extends ThrowingKnife
{
    public static final String ID = CreateFullID(ThrowingKnife_1.class.getSimpleName());

    public ThrowingKnife_1()
    {
        super(ID);

        Initialize(2,0, 1);
    }

    @Override
    protected void AddSecondaryEffect(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, this.magicNumber, false), this.magicNumber);
    }
}
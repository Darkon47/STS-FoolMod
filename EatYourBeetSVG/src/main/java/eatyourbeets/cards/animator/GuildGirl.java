package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.GuildGirlPower;

public class GuildGirl extends AnimatorCard
{
    public static final String ID = CreateFullID(GuildGirl.class.getSimpleName());

    public GuildGirl()
    {
        super(ID, 0, CardType.POWER, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new GuildGirlPower(p, 1), 1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            this.isInnate = true;
        }
    }
}
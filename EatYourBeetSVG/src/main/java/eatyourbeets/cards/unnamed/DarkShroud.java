package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActionsHelper;

public class DarkShroud extends UnnamedCard
{
    public static final String ID = CreateFullID(DarkShroud.class.getSimpleName());

    public DarkShroud()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0,5);

        this.exhaust = true;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        if (upgraded)
        {
            retain = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractOrb orb : p.orbs)
        {
            if (orb != null && orb.ID.equals(Dark.ORB_ID))
            {
                GameActionsHelper.ApplyPower(p, p, new EnchantedArmorPower(p, magicNumber), magicNumber);
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            this.retain = true;
        }
    }
}
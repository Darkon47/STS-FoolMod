package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.DrawSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Illya extends AnimatorCard
{
    public static final String ID = CreateFullID(Illya.class.getSimpleName());

    public Illya()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0,5);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractCard bestCard = null;
        int bestDamage = Integer.MIN_VALUE;
        for (AbstractCard c : p.drawPile.group)
        {
            if (c.type == CardType.ATTACK && c.baseDamage > bestDamage)
            {
                //logger.info(c.name + ", Damage: " + c.baseDamage+ ", Best Damage: " + bestDamage);
                bestDamage = c.baseDamage;
                bestCard = c;
            }
        }

        if (bestCard != null)
        {
            GameActionsHelper.AddToTop(new ModifyDamageAction(bestCard.uuid, this.magicNumber));
            GameActionsHelper.AddToTop(new DrawSpecificCardAction(bestCard));

            if (HasActiveSynergy())
            {
                bestCard.retain = true;
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(3);
        }
    }
}
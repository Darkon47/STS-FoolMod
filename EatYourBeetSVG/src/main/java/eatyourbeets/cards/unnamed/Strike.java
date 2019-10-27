package eatyourbeets.cards.unnamed;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.utilities.GameActionsHelper;

public class Strike extends UnnamedCard
{
    public static final String ID = CreateFullID(Strike.class.getSimpleName());

    public Strike()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);

        Initialize(6,0);

        this.tags.add(BaseModCardTags.BASIC_STRIKE);
        this.tags.add(AbstractCard.CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}
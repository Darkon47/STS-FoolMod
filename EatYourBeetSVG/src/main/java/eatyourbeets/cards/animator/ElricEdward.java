package eatyourbeets.cards.animator;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Earth;

import java.util.ArrayList;
import java.util.List;

public class ElricEdward extends AnimatorCard
{
    public static final String ID = Register(ElricEdward.class.getSimpleName(), EYBCardBadge.Special);

    public ElricEdward()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(4, 0, 1);

        AddExtendedDescription();

        SetEvokeOrbCount(1);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public List<TooltipInfo> getCustomTooltips()
    {
        if (cardText.index == 1)
        {
            return super.getCustomTooltips();
        }

        return null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper2.Cycle(1, name).AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                switch (cards.get(0).type)
                {
                    case ATTACK:
                        GameActionsHelper.ChannelOrb(new Lightning(), true);
                        break;

                    case SKILL:
                        GameActionsHelper.ChannelOrb(new Frost(), true);
                        break;

                    case POWER:
                        GameActionsHelper.ChannelOrb(new Earth(), true);
                        break;
                }
            }
        });
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }
}
package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.SupportDamagePower;

public class Guren extends AnimatorCard
{
    public static final String ID = CreateFullID(Guren.class.getSimpleName());

    public Guren()
    {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0);

        AddExtendedDescription();

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractCard attack = GetRandomAttack(p);
        if (attack != null)
        {
            int damage;
            attack.calculateCardDamage(null);
            damage = attack.damage;

            if (damage > 0)
            {
                ShowCardBrieflyEffect effect = new ShowCardBrieflyEffect(attack, Settings.WIDTH / 3f, Settings.HEIGHT / 2f);

                AbstractDungeon.effectsQueue.add(effect);
                GameActionsHelper.AddToBottom(new ExhaustSpecificCardAction(attack, p.drawPile, true));
                GameActionsHelper.AddToBottom(new WaitAction(effect.duration));

                if (upgraded)
                {
                    GameActionsHelper.GainBlock(p, damage);
                }

                GameActionsHelper.ApplyPower(p, p, new SupportDamagePower(p, damage), damage);
            }
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }

    private AbstractCard GetRandomAttack(AbstractPlayer p)
    {
        CardGroup attacks = p.drawPile.getAttacks();
        if (attacks.size() > 0)
        {
            return attacks.getRandomCard(true);
        }

        return null;
    }
}
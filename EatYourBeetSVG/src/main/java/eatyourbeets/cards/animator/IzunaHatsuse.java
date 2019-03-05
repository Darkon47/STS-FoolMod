package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class IzunaHatsuse extends AnimatorCard
{
    public static final String ID = CreateFullID(IzunaHatsuse.class.getSimpleName());

    private boolean transformed;

    public IzunaHatsuse()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(4, 2, 4);

        AddExtendedDescription();

        transformed = false;

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        AbstractPlayer p = AbstractDungeon.player;
        if ((p.currentHealth / (float)p.maxHealth) < 0.25f)
        {
            if (!transformed)
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID + "Alt"));
                this.type = CardType.ATTACK;
                this.target = CardTarget.SELF_AND_ENEMY;
                rawDescription = cardStrings.EXTENDED_DESCRIPTION[2];
                initializeDescription();
                transformed = true;
            }
        }
        else
        {
            if (transformed)
            {
                this.loadCardImage(AnimatorResources.GetCardImage(ID));
                this.type = CardType.SKILL;
                this.target = CardTarget.SELF;
                rawDescription = cardStrings.DESCRIPTION;
                initializeDescription();
                transformed = false;
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (transformed)
        {
            GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
            GameActionsHelper.AddToBottom(new HealAction(p, p, this.magicNumber));
        }
        else
        {
            GameActionsHelper.DrawCard(p, 1);
            GameActionsHelper.GainBlock(p, this.block);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
            upgradeDamage(2);
            upgradeBlock(1);
        }
    }
}
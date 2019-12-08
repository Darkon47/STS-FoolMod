package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.animator.SoraAction;
import eatyourbeets.actions._legacy.common.DrawSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.SoraEffects.SoraEffect;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.utilities.GameActionsHelper;
import patches.AbstractEnums;

public class Sora extends AnimatorCard
{
    public static final String ID = Register(Sora.class.getSimpleName(), EYBCardBadge.Special);

    public final SoraEffect effect;

    public Sora(SoraEffect effect, String name, String description)
    {
        super(staticCardData.get(ID), ID + "Alt", Resources_Animator.GetCardImage(ID + "Alt"),
                0, CardType.SKILL, AbstractEnums.Cards.THE_ANIMATOR, CardRarity.RARE, CardTarget.ALL);

        this.name = name;
        this.effect = effect;
        this.cardText.OverrideDescription(description, "-", true);
        //this.damageType = this.damageTypeForTurn = DamageInfo.DamageType.THORNS;
    }

    public Sora()
    {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);

        Initialize(0,0, 2);

        this.effect = null;

        SetMultiDamage(true);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractCard c : p.drawPile.group)
        {
            if (Shiro.ID.equals(c.cardID))
            {
                GameActionsHelper.AddToTop(new DrawSpecificCardAction(c));
                break;
            }
        }

        GameActionsHelper.AddToTop(new SoraAction(AbstractDungeon.player, magicNumber));
        GameActionsHelper.AddToTop(new WaitAction(0.4f));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return effect == null && super.canUpgrade();
    }
}
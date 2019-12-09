package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper_Legacy; import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Yuuichirou extends AnimatorCard
{
    public static final String ID = Register(Yuuichirou.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Yuuichirou()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(8,0);

        SetSynergy(Synergies.OwariNoSeraph);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new Asuramaru(), false);
        }
    }

//    @Override
//    public List<TooltipInfo> getCustomTooltips()
//    {
//        if (cardText.index == 1)
//        {
//            return super.getCustomTooltips();
//        }
//
//        return null;
//    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.Draw(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new Asuramaru(), false, false);

        AbstractDungeon.player.discardPile.addToTop(new Wound());
        AbstractDungeon.player.discardPile.addToTop(new Wound());
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
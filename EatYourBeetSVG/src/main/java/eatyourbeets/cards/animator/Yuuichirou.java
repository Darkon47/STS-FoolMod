package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Yuuichirou extends AnimatorCard
{
    public static final String ID = CreateFullID(Yuuichirou.class.getSimpleName());

    public Yuuichirou()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(8,0, 2);

        //AddExtendedDescription();

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        PlayerStatistics.ApplyTemporaryDexterity(p, p, magicNumber);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        Asuramaru card = new Asuramaru();
        if (upgraded)
        {
            card.upgrade();
        }

        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(card, 1));
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
package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnBattleStartSubscriber;
import eatyourbeets.subscribers.OnCardDrawnSubscriber;

public class ChlammyZell extends AnimatorCard
{
    public static final String ID = CreateFullID(ChlammyZell.class.getSimpleName());

    public ChlammyZell()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(16, 0, 4);

        this.isMultiDamage = true;

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < this.multiDamage.length; i++)
        {
            if (this.magicNumber < this.multiDamage[i])
            {
                this.multiDamage[i] = AbstractDungeon.miscRng.random(this.magicNumber, this.multiDamage[i]);
            }
            else
            {
                this.multiDamage[i] = this.magicNumber;
            }
        }

        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
            upgradeDamage(5);
        }
    }
}
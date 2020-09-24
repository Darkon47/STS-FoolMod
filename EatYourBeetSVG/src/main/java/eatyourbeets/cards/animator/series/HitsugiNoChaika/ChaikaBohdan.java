package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnAttackSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ChaikaBohdan extends AnimatorCard implements OnAttackSubscriber
{
    public static final EYBCardData DATA = Register(ChaikaBohdan.class).SetAttack(1, CardRarity.COMMON);

    private int bonusDamage = 0;

    public ChaikaBohdan()
    {
        super(DATA);

        Initialize(6, 0, 3, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (player.hand.contains(this))
        {
            CombatStats.onAttack.Subscribe(this);
        }
    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (player.hand.contains(this))
        {
            if (info.type == DamageInfo.DamageType.NORMAL && GameUtilities.IsMonster(target))
            {
                this.AddDamageBonus(this.secondaryValue);
                this.flash();
            }
        }
        else
        {
            CombatStats.onAttack.Unsubscribe(this);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        AddDamageBonus(-bonusDamage);
    }

    private void AddDamageBonus(int amount)
    {
        bonusDamage += amount;
        baseDamage += amount;
    }
}
package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.utilities.PCLActions;

public class BaseEffect_DealCardDamage extends BaseEffect
{
    public static final String ID = Register(BaseEffect_DealCardDamage.class);

    protected final AbstractGameAction.AttackEffect attackEffect;

    public BaseEffect_DealCardDamage(PCLCard card, AbstractGameAction.AttackEffect attackEffect)
    {
        super(ID, attackEffect.name(), card.attackTarget, 0);
        this.attackEffect = attackEffect;
        SetSourceCard(card, PCLCardValueSource.Damage);
    }

    @Override
    public String GetText()
    {
        return null;
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (target == PCLCardTarget.AoE || target == PCLCardTarget.All) {
            PCLActions.Bottom.DealCardDamageToAll(sourceCard, attackEffect);
        }
        else {
            PCLActions.Bottom.DealCardDamage(sourceCard, m, attackEffect);
        }
    }

    @Override
    public AbstractAttribute GetDamageInfo() {
        return sourceCard.GetDamageInfo();
    }
}

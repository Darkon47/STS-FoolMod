package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericCardEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class GenericCardEffect_DealDamageToAll extends GenericCardEffect
{
    public static final String ID = Register(GenericCardEffect_DealDamageToAll.class);

    protected final AbstractGameAction.AttackEffect attackEffect;

    public GenericCardEffect_DealDamageToAll(int amount, AbstractGameAction.AttackEffect attackEffect)
    {
        super(ID, attackEffect.name(), PCLCardTarget.AoE, amount);
        this.attackEffect = attackEffect;
    }

    @Override
    public String GetText()
    {
        return PGR.PCL.Strings.Actions.DealDamageToAll(amount, true);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int[] damage = DamageInfo.createDamageMatrix(amount, true, true);
        PCLActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, attackEffect);
    }
}

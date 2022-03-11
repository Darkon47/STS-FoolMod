package pinacolada.cards.base.baseeffects.effects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.DamageAttribute;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;

public class BaseEffect_DealDamage extends BaseEffect
{
    public static final String ID = Register(BaseEffect_DealDamage.class);

    protected final AbstractGameAction.AttackEffect attackEffect;

    public BaseEffect_DealDamage() {
        this(0, AbstractGameAction.AttackEffect.NONE);
    }

    public BaseEffect_DealDamage(SerializedData content)
    {
        super(content);
        this.attackEffect =  AbstractGameAction.AttackEffect.NONE;
    }

    public BaseEffect_DealDamage(int amount, AbstractGameAction.AttackEffect attackEffect) {
        this(amount, attackEffect, PCLCardTarget.Normal);
    }

    public BaseEffect_DealDamage(int amount, AbstractGameAction.AttackEffect attackEffect, PCLCardTarget target)
    {
        super(ID, attackEffect.name(), PCLCardTarget.Normal, amount);
        this.attackEffect = attackEffect;
    }

    @Override
    public String GetText()
    {
        if (target == PCLCardTarget.Normal) {
            return PGR.PCL.Strings.Actions.DealDamage(amount, true);
        }
        return PGR.PCL.Strings.Actions.DealDamageTo(amount, GetTargetString(), true);
    }

    @Override
    public String GetSampleText()
    {
        return PGR.PCL.Strings.Actions.DealDamage("X", false);
    }

    @Override
    public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (target == PCLCardTarget.AoE || target == PCLCardTarget.All) {
            int[] damage = DamageInfo.createDamageMatrix(amount, true, true);
            PCLActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, attackEffect);
        }
        else {
            PCLActions.Bottom.DealDamage(p, m, amount, DamageInfo.DamageType.THORNS, attackEffect);
        }
    }

    @Override
    public AbstractAttribute GetDamageInfo() {
        return DamageAttribute.Instance
                .SetIcon(PGR.PCL.Images.Icons.Damage.Texture())
                .SetLargeIcon(PGR.PCL.Images.Icons.Damage_L.Texture())
                .SetText(new ColoredString(amount, Settings.CREAM_COLOR));
    }
}

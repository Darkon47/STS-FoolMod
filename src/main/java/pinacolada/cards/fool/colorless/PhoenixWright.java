package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.base.baseeffects.CompositeEffect;
import pinacolada.cards.fool.FoolCard;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.ui.cards.TargetEffectPreview;
import pinacolada.utilities.PCLGameUtilities;

public class PhoenixWright extends FoolCard
{
    public static final PCLCardData DATA = Register(PhoenixWright.class)
            .SetSkill(-1, CardRarity.UNCOMMON)
            .SetColorless().SetSeries(CardSeries.PhoenixWright);

    private TargetEffectPreview effectPreview = new TargetEffectPreview(this::ChangeEffect);
    private BaseEffect currentEffect = null;

    public PhoenixWright()
    {
        super(DATA);

        Initialize(5, 4, 3);
        SetUpgrade(1, 1, 1);

        SetAffinity_Orange(1);
        SetAffinity_Light(1);

        SetAttackType(PCLAttackType.Normal);
        SetExhaust(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (currentEffect != null)
        {
            currentEffect.OnDrag(m);
        }
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return (currentEffect != null) ? currentEffect.GetDamageInfo() : null;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return (currentEffect != null) ? currentEffect.GetBlockInfo() : null;
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return (currentEffect != null) ? currentEffect.GetSpecialInfo() : null;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        super.calculateCardDamage(mo);

        targetDrawScale = 1f;
        target_x = Settings.WIDTH * 0.4f;
        target_y = Settings.HEIGHT * 0.4f;
        effectPreview.SetCurrentTarget(mo);
    }

    @Override
    public void update()
    {
        super.update();

        effectPreview.Update();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        energyOnUse = PCLGameUtilities.UseXCostEnergy(this);
        currentEffect.Use(p, m, info);
    }

    private void ChangeEffect(AbstractMonster enemy)
    {
        energyOnUse = -1;

        if (enemy != null)
        {
            energyOnUse = PCLGameUtilities.GetXCostEnergy(this);
            currentEffect = GetEffect(enemy.intent, energyOnUse + 1);
            cardText.OverrideDescription(currentEffect.GetText(), true);
        }
        else
        {
            currentEffect = null;
            cardText.OverrideDescription(null, true);
        }
    }

    private BaseEffect GetEffect(AbstractMonster.Intent intent, int effectPower) {
        switch (intent) {
            case ATTACK:
                return BaseEffect.GainBlock(effectPower * baseBlock);
            case ATTACK_BUFF:
                return new CompositeEffect(
                        BaseEffect.GainBlock(effectPower * baseBlock),
                        BaseEffect.GainAffinityPower(effectPower, PCLAffinity.Orange)
                );
            case ATTACK_DEBUFF:
                return new CompositeEffect(
                        BaseEffect.GainBlock(effectPower * baseBlock),
                        BaseEffect.ApplyToSingle(effectPower, PCLPowerHelper.Weak)
                );
            case ATTACK_DEFEND:
                return new CompositeEffect(
                        BaseEffect.DealDamage(effectPower * baseDamage),
                        BaseEffect.GainBlock(effectPower * baseBlock)
                );
            case BUFF:
                return BaseEffect.GainAffinityPower(effectPower + 1, PCLAffinity.Orange);
            case DEBUFF:
                return BaseEffect.ApplyToSingle(effectPower, PCLPowerHelper.Weak, PCLPowerHelper.Vulnerable);
            case STRONG_DEBUFF:
                return BaseEffect.ApplyToSingle(effectPower, PCLPowerHelper.Weak, PCLPowerHelper.Vulnerable, PCLPowerHelper.Blinded);
            case DEFEND:
                return BaseEffect.DealDamage(effectPower * baseDamage);
            case DEFEND_DEBUFF:
                return new CompositeEffect(
                        BaseEffect.DealDamage(effectPower * baseDamage),
                        BaseEffect.ApplyToSingle(effectPower, PCLPowerHelper.Vulnerable)
                );
            case DEFEND_BUFF:
                return new CompositeEffect(
                        BaseEffect.DealDamage(effectPower * baseDamage),
                        BaseEffect.GainAffinityPower(effectPower, PCLAffinity.Orange)
                );
            case ESCAPE:
                return new CompositeEffect(
                        BaseEffect.DealDamage(effectPower * baseDamage),
                        BaseEffect.Stun(1)
                );
            case SLEEP:
                return BaseEffect.Gain(effectPower, PCLPowerHelper.Energized);
            case UNKNOWN:
                return BaseEffect.GainTempHP(effectPower * baseMagicNumber);
            case DEBUG:
            case NONE:
            case STUN:
                return BaseEffect.Gain(effectPower, PCLPowerHelper.NextTurnDraw);
            case MAGIC:
            default:
                return BaseEffect.Gain(effectPower + baseMagicNumber, PCLPowerHelper.Sorcery);
        }
    }
}
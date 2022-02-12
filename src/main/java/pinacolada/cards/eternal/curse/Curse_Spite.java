package pinacolada.cards.eternal.curse;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Curse_Spite extends EternalCard
{
    public static final PCLCardData DATA = Register(Curse_Spite.class)
            .SetCurse(-2, PCLCardTarget.None, false, true);

    public Curse_Spite()
    {
        super(DATA);

        Initialize(0, 0, 5, 1);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();
        DoEffect();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        DoEffect();
    }

    @Override
    public void triggerOnPurge()
    {
        super.triggerOnPurge();
        DoEffect();
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            PCLActions.Bottom.TakeDamage(secondaryValue, AttackEffects.DARKNESS);
        }
    }

    protected void DoEffect() {
        int[] damage = DamageInfo.createDamageMatrix(magicNumber, true, true);
        PCLActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.HP_LOSS, AttackEffects.DARKNESS);
    }
}
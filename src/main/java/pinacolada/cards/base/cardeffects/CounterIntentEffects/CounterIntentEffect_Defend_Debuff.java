package pinacolada.cards.base.cardeffects.CounterIntentEffects;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.effects.AttackEffects;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class CounterIntentEffect_Defend_Debuff extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            PCLActions.Bottom.DealDamage(p, m, damage, DamageInfo.DamageType.THORNS, AttackEffects.BLUNT_LIGHT);
            PCLGameUtilities.RemoveDamagePowers();
        }

        PCLActions.Bottom.ApplyVulnerable(p, m, GetVulnerable(nanami));
    }

    @Override
    public String GetDescription(PCLCard nanami)
    {
        return ACTIONS.Apply(GetVulnerable(nanami), PGR.Tooltips.Vulnerable, true);
    }

    @Override
    public int GetDamage(PCLCard nanami)
    {
        if (nanami.energyOnUse > 0)
        {
            return ModifyDamage((nanami.energyOnUse + 1) * nanami.baseDamage, nanami);
        }
        else
        {
            return 0;
        }
    }

    private int GetVulnerable(PCLCard nanami)
    {
        return nanami.energyOnUse + 1;
    }
}
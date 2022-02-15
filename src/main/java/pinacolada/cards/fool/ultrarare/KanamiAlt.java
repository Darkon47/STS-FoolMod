package pinacolada.cards.fool.ultrarare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import eatyourbeets.interfaces.markers.Hidden;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard_UltraRare;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class KanamiAlt extends FoolCard_UltraRare implements Hidden
{
    public static final PCLCardData DATA = Register(KanamiAlt.class)
            .SetAttack(2, CardRarity.SPECIAL, PCLAttackType.Normal)
            .SetColorless()
            .SetSeries(CardSeries.LogHorizon);

    public KanamiAlt()
    {
        super(DATA);

        Initialize(20, 2, 10);
        SetUpgrade(7, 0, 0);

        SetAffinity_Red(1, 0, 2);
        SetAffinity_Green(1);
        SetAffinity_Light(1);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        if (enemy != null && enemy.hasPower(VulnerablePower.POWER_ID))
        {
            amount += magicNumber;
        }

        return super.ModifyBlock(enemy, amount);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null)
        {
            if (enemy.type == AbstractMonster.EnemyType.ELITE)
            {
                amount *= 2;
            }
            if (enemy.type == AbstractMonster.EnemyType.BOSS)
            {
                amount *= 3;
            }
        }

        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.RED.cpy()));
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d
        .AddCallback(block, (amount, __) ->
        {
            if (amount > 0)
            {
                PCLActions.Bottom.GainMight(1);
                PCLActions.Bottom.GainVelocity(1);
                PCLActions.Bottom.GainBlock(amount);
            }
        }));
    }
}
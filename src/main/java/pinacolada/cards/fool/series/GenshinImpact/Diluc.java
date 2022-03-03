package pinacolada.cards.fool.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.PCLEffekseerEFX;
import pinacolada.effects.VFX;
import pinacolada.powers.common.BurningWeaponPower;
import pinacolada.powers.common.FreezingPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Diluc extends FoolCard
{
    public static final PCLCardData DATA = Register(Diluc.class).SetAttack(3, CardRarity.UNCOMMON, PCLAttackType.Fire, PCLCardTarget.AoE).SetSeriesFromClassPackage(true);

    public Diluc()
    {
        super(DATA);

        Initialize(13, 0, 2, 3);
        SetUpgrade(2,0,1,0);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Orange(0, 0, 1);

        SetAffinityRequirement(PCLAffinity.Red, 8);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(VFX.EFX(PCLEffekseerEFX.FIRE02).SetScale(2f));
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY).forEach(d -> d.SetVFXColor(Color.FIREBRICK));

        int amount = 0;
        for (AbstractCreature c : PCLGameUtilities.GetEnemies(true)) {
            if (c.hasPower(FreezingPower.POWER_ID)) {
                amount += magicNumber;
            }
        }
        if (amount > 0) {
            PCLActions.Bottom.StackPower(new NextTurnBlockPower(p, amount));
        }

        if (TrySpendAffinity(PCLAffinity.Red)) {
            PCLActions.Bottom.StackPower(new BurningWeaponPower(p, secondaryValue));
        }
    }
}


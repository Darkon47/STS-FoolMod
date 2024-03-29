package pinacolada.cards.fool.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.Xiangling_Guoba;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.common.FreezingPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Xiangling extends FoolCard
{
    public static final PCLCardData DATA = Register(Xiangling.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Fire, PCLCardTarget.AoE).SetSeriesFromClassPackage(true)
            .PostInitialize(data -> data.AddPreview(new Xiangling_Guoba(), false));

    public Xiangling()
    {
        super(DATA);

        Initialize(5, 0, 2, 0);
        SetUpgrade(3, 0, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Green(0, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HORIZONTAL).forEach(d -> d.SetVFXColor(Color.FIREBRICK));

        for (AbstractMonster enemy : PCLGameUtilities.GetEnemies(true)) {
            if (PCLGameUtilities.GetPowerAmount(enemy, BurningPower.POWER_ID) > 0 || PCLGameUtilities.GetPowerAmount(enemy, FreezingPower.POWER_ID) > 0) {
                PCLActions.Bottom.GainTemporaryHP(magicNumber);
            }
        }

        if (info.IsSynergizing && info.TryActivateSemiLimited()) {
            PCLActions.Bottom.MakeCardInDrawPile(new Xiangling_Guoba());
        }
    }
}


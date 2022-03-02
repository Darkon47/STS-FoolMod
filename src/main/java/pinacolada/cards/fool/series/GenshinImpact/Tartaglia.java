package pinacolada.cards.fool.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.curse.Curse_Delusion;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.BurningPower;
import pinacolada.stances.pcl.MightStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Tartaglia extends FoolCard {
    public static final PCLCardData DATA = Register(Tartaglia.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Ranged, PCLCardTarget.AoE).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_Delusion(), false));

    public Tartaglia() {
        super(DATA);

        Initialize(12, 0, 4, 1);
        SetUpgrade(3, 0, 1);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);

        SetCooldown(2, 0, this::OnCooldownCompleted);
    }

    @Override
    protected float GetInitialDamage()
    {
        int amount = 0;
        for (AbstractCreature c : PCLGameUtilities.GetAllCharacters(true)) {
            if (PCLGameUtilities.GetPowerAmount(c, BurningPower.POWER_ID) > 0) {
                amount += magicNumber;
            }
        }
        return baseDamage + amount;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.BLUNT_LIGHT);

        for (AbstractCreature c : PCLGameUtilities.GetAllCharacters(true)) {
            PCLActions.Bottom.RemovePower(p, c, BurningPower.POWER_ID);
        }

        PCLActions.Bottom.ApplyRippled(TargetHelper.Enemies(), secondaryValue);
        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Bottom.MakeCardInDrawPile(new Curse_Delusion());
        PCLActions.Bottom.ChangeStance(MightStance.STANCE_ID);
    }
}
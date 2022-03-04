package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLPowerHelper;

public class TranscendenceForm extends EternalCard
{
    public static final PCLCardData DATA = Register(TranscendenceForm.class).SetSkill(5, CardRarity.RARE, PCLCardTarget.Self);
    private static final CardEffectChoice choices = new CardEffectChoice();

    public TranscendenceForm()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 1, 0);

        SetLight();
        SetPurge(true);
    }

    @Override
    protected void OnUpgrade() {
        super.OnUpgrade();
        SetRetainOnce(true);
    }

    @Override
    public int GetXValue() {
        return magicNumber + secondaryValue;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (choices.TryInitialize(this))
        {
            choices.AddEffect(BaseEffect.Gain(magicNumber, PCLPowerHelper.Ritual));
            choices.AddEffect(BaseEffect.Gain(secondaryValue, PCLPowerHelper.Intangible));
            choices.AddEffect(BaseEffect.Gain(GetXValue(), PCLPowerHelper.Artifact));
        }
        choices.Select(CheckSpecialCondition(true) ? 2 : 1, m);
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return !CheckPrimaryCondition(tryUse) && tryUse ? CombatStats.TryActivateLimited(cardID) : CombatStats.CanActivateLimited(cardID);
    }
}
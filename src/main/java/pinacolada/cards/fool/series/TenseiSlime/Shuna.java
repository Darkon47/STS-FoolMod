package pinacolada.cards.fool.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.fool.FoolCard;
import pinacolada.utilities.PCLActions;

public class Shuna extends FoolCard
{
    public static final PCLCardData DATA = Register(Shuna.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetTraits(PCLCardTrait.Beast, PCLCardTrait.Spellcaster)
            .SetSeriesFromClassPackage();

    public Shuna()
    {
        super(DATA);

        Initialize(0, 1, 1, 2);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Light, 5);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.GainTemporaryHP(secondaryValue);
            PCLActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.Draw(secondaryValue);
        if (TrySpendAffinity(PCLAffinity.Light)) {
            PCLActions.Bottom.Draw(1);
        }
    }
}
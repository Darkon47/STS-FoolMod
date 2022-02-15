package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class PolaritySwitch extends EternalCard
{
    public static final PCLCardData DATA = Register(PolaritySwitch.class).SetSkill(0, CardRarity.RARE);

    public PolaritySwitch()
    {
        super(DATA);

        Initialize(0, 0, 2, 0);
        SetUpgrade(0, 0, 0, 0);

        SetRetainOnce(true);
        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        super.OnUpgrade();
        SetExhaust(false);
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromHand(name, 999, true)
                .SetOptions(true, true, true)
                .AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        if (c instanceof PCLCard) {
                            if (PCLGameUtilities.HasDarkAffinity(c)) {
                                ((PCLCard) c).affinities.Set(PCLAffinity.Light, 1);
                                ((PCLCard) c).affinities.Set(PCLAffinity.Dark, 0);
                            }
                            else if (PCLGameUtilities.HasLightAffinity(c)) {
                                ((PCLCard) c).affinities.Set(PCLAffinity.Light, 0);
                                ((PCLCard) c).affinities.Set(PCLAffinity.Dark, 1);
                            }
                        }
                    }
                });
        if (CheckPrimaryCondition(true)) {
            PCLActions.Bottom.GainSupportDamage(magicNumber);
        }
    }
}
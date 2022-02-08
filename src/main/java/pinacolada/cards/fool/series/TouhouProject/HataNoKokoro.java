package pinacolada.cards.fool.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.utilities.PCLActions;

public class HataNoKokoro extends FoolCard {
    public static final PCLCardData DATA = Register(HataNoKokoro.class)
            .SetSkill(1, CardRarity.RARE, PCLCardTarget.None)
            .SetSeriesFromClassPackage(true);

    public HataNoKokoro() {
        super(DATA);

        Initialize(0, 2, 3, 2);
        SetUpgrade(0, 0, 2, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.General, 5);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && CheckAffinity(PCLAffinity.General);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.AddAffinity(PCLAffinity.Blue, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback((chosenAffinities) -> {
            for (AffinityChoice choice : chosenAffinities) {
                PCLActions.Bottom.SelectFromHand(name, player.hand.size() - 1, true)
                        .SetOptions(true, true, true)
                        .AddCallback((cards) -> {
                            for (AbstractCard c : cards) {
                                if (c instanceof PCLCard) {
                                    PCLCardAffinities newAffinities = new PCLCardAffinities(null);
                                    newAffinities.Set(choice.Affinity, 1);
                                    if (((PCLCard) c).CanScale()) {
                                        newAffinities.SetScaling(choice.Affinity, magicNumber);
                                    }
                                    for (PCLCardAffinity cAff : affinities.List) {
                                        newAffinities.SetRequirement(cAff.type, cAff.requirement);
                                    }
                                    ((PCLCard) c).affinities.Initialize(newAffinities);
                                }
                            }
                        });
            }
        });
    }
}


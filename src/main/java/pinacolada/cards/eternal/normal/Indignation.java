package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.cards.eternal.curse.Curse_Spite;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Indignation extends EternalCard
{
    public static final PCLCardData DATA = Register(Indignation.class).SetAttack(2, CardRarity.RARE, PCLAttackType.Piercing, PCLCardTarget.AoE)
            .PostInitialize(data -> data.AddPreview(new Curse_Spite(), false));

    public Indignation()
    {
        super(DATA);

        Initialize(27, 0, 2);
        SetUpgrade(5, 0);

        SetDark();
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.DARKNESS);
        if (info.IsSynergizing)
        {
            for (int i = 0; i < magicNumber; i++) {
                PCLActions.Bottom.MakeCardInHand(new Curse_Spite());
            }
        }
        else {
            for (int i = 0; i < magicNumber; i++) {
                PCLActions.Bottom.MakeCardInDrawPile(new Curse_Spite())
                        .SetDestination(CardSelection.Top);
            }

        }
    }
}
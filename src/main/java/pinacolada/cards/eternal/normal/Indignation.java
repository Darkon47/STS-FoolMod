package pinacolada.cards.eternal.normal;

import com.badlogic.gdx.graphics.Color;
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
import pinacolada.effects.PCLEffekseerEFX;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;

public class Indignation extends EternalCard
{
    public static final PCLCardData DATA = Register(Indignation.class).SetAttack(2, CardRarity.RARE, PCLAttackType.Piercing, PCLCardTarget.AoE)
            .PostInitialize(data -> data.AddPreview(new Curse_Spite(), false));

    public Indignation()
    {
        super(DATA);

        Initialize(13, 0, 2);
        SetUpgrade(3, 0);
        SetHitCount(2);

        SetLight();
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(VFX.EFX(PCLEffekseerEFX.LIGHT04).SetColor(Color.PURPLE));
        PCLActions.Bottom.SFX(SFX.PCL_DARKNESS);
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.NONE);
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
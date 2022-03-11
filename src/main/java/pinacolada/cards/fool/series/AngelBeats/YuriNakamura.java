package pinacolada.cards.fool.series.AngelBeats;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.PCLEffekseerEFX;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;

import static pinacolada.resources.PGR.Enums.CardTags.AFTERLIFE;

public class YuriNakamura extends FoolCard
{
    public static final PCLCardData DATA = Register(YuriNakamura.class).SetAttack(2, CardRarity.RARE, PCLAttackType.Ranged).SetSeriesFromClassPackage();

    public YuriNakamura()
    {
        super(DATA);

        Initialize(4, 8, 2, 5);
        SetUpgrade(1, 2,0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Light(1, 0, 2);
        SetAffinity_Orange(1, 0, 1);
        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Light, 5);
        SetHitCount(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(VFX.EFX(PCLEffekseerEFX.GUN03, m.hb));
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);


        CardGroup[] groups = upgraded ? (new CardGroup[] {p.hand, p.discardPile, p.drawPile}) : (new CardGroup[] {p.hand});
        PCLActions.Bottom.ExhaustFromPile(name, magicNumber, groups)
                .SetOptions(false, true)
                .AddCallback(cards -> {
                    PCLActions.Bottom.GainBlock(cards.size() * secondaryValue);
        });

        if (player.exhaustPile.size() > 0 && (TrySpendAffinity(PCLAffinity.Light))) {
            for (int i = 0; i < magicNumber; i++) {
                PCLActions.Last.Motivate(player.exhaustPile).SetFilter(c -> c.hasTag(AFTERLIFE));
            }
        }
    }
}
package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.pcl.curse.Curse_Normality;
import pinacolada.powers.fool.PhasingPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class NaoTomori extends FoolCard
{
    public static final PCLCardData DATA = Register(NaoTomori.class).SetSkill(1, CardRarity.RARE, PCLCardTarget.None).SetMaxCopies(1).SetColorless()
            .SetSeries(CardSeries.Charlotte)
            .PostInitialize(data -> data.AddPreview(new Curse_Normality(), false));

    public NaoTomori()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 0);

        SetAffinity_Green(1);
        SetAffinity_Orange(1);
        SetAffinity_Light(1);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetRetain(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        while (group.size() < magicNumber)
        {
            AbstractCard card = PCLGameUtilities.GetRandomCard();
            if (group.findCardById(card.cardID) == null)
            {
                if (upgraded) {
                    PCLGameUtilities.ModifyCostForCombat(card, -1, true);
                }
                else {
                    PCLGameUtilities.ModifyCostForTurn(card, -1, true);
                }
                group.addToBottom(card.makeCopy());
            }
        }

        PCLActions.Bottom.SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                PCLActions.Bottom.MakeCardInHand(cards.get(0));
                PCLGameUtilities.RefreshHandLayout();
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromHand(name, 1, false)
        .SetFilter(c -> c.type.equals(CardType.POWER) || c.type.equals(CardType.STATUS) || (upgraded && c.type.equals(CardType.CURSE)))
        .SetOptions(false, false, false)
        .SetMessage(DATA.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                PCLActions.Bottom.StackPower(new PhasingPower(player, secondaryValue));
                PCLActions.Last.ReplaceCard(cards.get(0).uuid, new Curse_Normality());
            }
        });
    }
}